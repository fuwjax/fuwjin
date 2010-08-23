/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import static org.fuwjin.pogo.postage.PostageUtils.buffer;
import static org.fuwjin.pogo.postage.PostageUtils.invoke;
import static org.fuwjin.pogo.postage.PostageUtils.isCustomFunction;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.BufferedPosition;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.Memo;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Position;
import org.fuwjin.postage.Category;
import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;

/**
 * A grammar rule.
 */
public class Rule implements org.fuwjin.pogo.Rule {
   private String name;
   private Parser parser;
   private Function finalizer;
   private Category type;
   private Function initializer;
   private Function serializer;
   private boolean simple = true;

   /**
    * Creates a new instance.
    */
   Rule() {
      type("default");
      initializer("default");
      serializer("default");
      finalizer("default");
   }

   /**
    * Creates a new instance.
    * @param name the name
    * @param type the bound type
    * @param initializer the rule initializer
    * @param finalizer the rule finalizer
    * @param parser the expression
    */
   public Rule(final String name, final String type, final String initializer, final String serializer,
         final String finalizer, final Parser parser) {
      this.name = name;
      type(type);
      initializer(initializer);
      serializer(serializer);
      finalizer(finalizer);
      this.parser = parser;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final Rule o = (Rule)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name) && eq(type.name(), o.type.name())
               && eq(initializer.name(), o.initializer.name()) && eq(serializer.name(), o.serializer.name())
               && eq(finalizer.name(), o.finalizer.name()) && eq(parser, o.parser);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   private void finalizer(final String finalizer) {
      this.finalizer = new Partial(finalizer).as(Function.class);
   }

   @Override
   public Function getFunction(final String name) {
      return type.getFunction(name);
   }

   @Override
   public int hashCode() {
      return hash(getClass(), name, type, initializer, finalizer, parser);
   }

   private void initializer(final String initializer) {
      this.initializer = new Partial(initializer).as(Function.class);
   }

   /**
    * Returns the name.
    * @return the name
    */
   @Override
   public String name() {
      return name;
   }

   @Override
   public Position parse(final Position position) {
      if(simple) {
         return parser.parse(position);
      }
      final BufferedPosition pos = buffer(position, serializer);
      final Memo memo = pos.memo();
      Object result = invoke(initializer, memo.getValue());
      if(result instanceof Failure) {
         pos.fail("could not initialize rule " + name, (Failure)result);
      } else {
         memo.setValue(result);
         final Position next = parser.parse(pos);
         if(next.isSuccess()) {
            result = invoke(serializer, memo.getValue(), pos.match(next));
            if(result instanceof Failure) {
               next.fail("could not handle rule match" + name, (Failure)result);
            } else {
               result = invoke(finalizer, result);
               if(result instanceof Failure) {
                  next.fail("could not finalize rule " + name, (Failure)result);
               } else {
                  memo.setValue(result);
                  return pos.flush(next);
               }
            }
         }
      }
      return pos.flush(pos);
   }

   /**
    * Resolves this rule and the inner expression.
    * @param grammar the grammar to resolve rule references
    */
   @Override
   public void resolve(final Grammar grammar, final org.fuwjin.pogo.Rule parent) {
      type = grammar.resolve(Partial.<String> content(type));
      initializer = type.getFunction(Partial.<String> content(initializer));
      serializer = type.getFunction(Partial.<String> content(serializer));
      finalizer = type.getFunction(Partial.<String> content(finalizer));
      simple = !isCustomFunction(initializer) && !isCustomFunction(serializer) && !isCustomFunction(finalizer);
      parser.resolve(grammar, this);
   }

   private void serializer(final String serializer) {
      this.serializer = new Partial(serializer).as(Function.class);
   }

   @Override
   public String toString() {
      return name + " <- " + parser;
   }

   private void type(final String type) {
      this.type = new Partial(type).as(Category.class);
   }
}
