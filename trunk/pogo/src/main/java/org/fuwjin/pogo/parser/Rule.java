/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.util.Map;

import org.fuwjin.io.BufferedPosition;
import org.fuwjin.io.Position;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.reflect.DefaultResultTask;
import org.fuwjin.pogo.reflect.FinalizerTask;
import org.fuwjin.pogo.reflect.InitializerTask;
import org.fuwjin.pogo.reflect.ObjectType;
import org.fuwjin.pogo.reflect.ReferenceTask;
import org.fuwjin.pogo.reflect.ReflectionType;

/**
 * A grammar rule.
 */
public class Rule implements Parser {
   private String name;
   private Parser parser;
   private FinalizerTask finalizer = new DefaultResultTask();
   private ReflectionType type = new ObjectType();
   private InitializerTask initializer = new ReferenceTask();

   /**
    * Creates a new instance.
    */
   Rule() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param name the name
    * @param type the bound type
    * @param initializer the rule initializer
    * @param finalizer the rule finalizer
    * @param parser the expression
    */
   public Rule(final String name, final ReflectionType type, final InitializerTask initializer,
         final FinalizerTask finalizer, final Parser parser) {
      this.name = name;
      this.type = type;
      this.initializer = initializer;
      this.finalizer = finalizer;
      this.parser = parser;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final Rule o = (Rule)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name) && eq(type, o.type) && eq(initializer, o.initializer)
               && eq(finalizer, o.finalizer) && eq(parser, o.parser);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), name, type, initializer, finalizer, parser);
   }

   private BufferedPosition initialize(final Position position) {
      final Object root = position.fetch(null);
      final Object element = position.fetch(name);
      final Object newElement = initializer.initialize(root, element);
      if(newElement instanceof Throwable) {
         position.fail("could not initialize rule " + name, (Throwable)newElement);
      } else {
         position.store(name, newElement);
         if(finalizer.canMatch(newElement)) {
            return position.buffered();
         }
      }
      return position.unbuffered();
   }

   /**
    * Returns the name.
    * @return the name
    */
   public String name() {
      return name;
   }

   @Override
   public Position parse(final Position position) {
      final BufferedPosition pos = initialize(position);
      if(pos.isSuccess()) {
         final Position next = parser.parse(pos);
         if(next.isSuccess()) {
            final Object root = next.fetch(null);
            final Object finishedElement = next.fetch(name);
            final Object finalElement = finalizer.finalize(root, finishedElement, pos.match(next));
            if(finalElement instanceof Throwable) {
               pos.fail("could not finalize rule " + name, (Throwable)finalElement);
            } else {
               next.store(name, finalElement);
               return pos.flush(next);
            }
         } else {
            pos.fail(next);
         }
      }
      return pos.flush(pos);
   }

   /**
    * Resolves this rule and the inner expression.
    * @param grammar the grammar to resolve rule references
    */
   @Override
   public void resolve(final String parent, final Map<String, Rule> grammar, final ReflectionType ignore) {
      initializer.setType(type);
      finalizer.setType(type);
      parser.resolve(name, grammar, type);
   }

   @Override
   public String toString() {
      return name + " <- " + parser;
   }
}
