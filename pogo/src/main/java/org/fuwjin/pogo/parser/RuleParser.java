/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.pogo.parser;

import static org.fuwjin.pogo.postage.PostageUtils.isCustomFunction;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Rule;
import org.fuwjin.pogo.state.PogoPosition;
import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.postage.CompositeFunction;
import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.type.Optional;

/**
 * A grammar rule.
 */
public class RuleParser implements Rule {
   private String name;
   private Parser parser;
   private Function finalizer;
   private final String type;
   private Function initializer;
   private Function serializer;
   private boolean simple = true;

   /**
    * Creates a new instance.
    */
   RuleParser() {
      type = "default";
      initializer = new CompositeFunction("default");
      serializer = new CompositeFunction("default");
      finalizer = new CompositeFunction("default");
   }

   /**
    * Creates a new instance.
    * @param name the name
    * @param type the bound type
    * @param initializer the rule initializer
    * @param serializer the rule serializer
    * @param finalizer the rule finalizer
    * @param parser the expression
    */
   public RuleParser(final String name, final String type, final String initializer, final String serializer,
         final String finalizer, final Parser parser) {
      this.name = name;
      this.type = type;
      this.initializer = new CompositeFunction(initializer);
      this.serializer = new CompositeFunction(serializer);
      this.finalizer = new CompositeFunction(finalizer);
      this.parser = parser;
   }

   @Override
   public String category() {
      return type;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final RuleParser o = (RuleParser)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name) && eq(type, o.type)
               && eq(initializer.name(), o.initializer.name()) && eq(serializer.name(), o.serializer.name())
               && eq(finalizer.name(), o.finalizer.name()) && eq(parser, o.parser);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), name, type, initializer, finalizer, parser);
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
   public boolean parse(final PogoState state) {
      if(simple) {
         return parser.parse(state);
      }
      boolean success = false;
      Object result = initializer.invokeSafe(state.getValue());
      if(result instanceof Failure) {
         state.fail("could not initialize rule " + name, (Failure)result);
      } else {
         final PogoPosition buffer = state.buffer(isCustomFunction(serializer));
         state.setValue(result);
         if(parser.parse(state)) {
            result = serializer.invokeSafe(state.getValue(), buffer.toString());
            if(result instanceof Failure) {
               state.fail("could not handle rule match" + name, (Failure)result);
            } else {
               result = finalizer.invokeSafe(result);
               if(result instanceof Failure) {
                  state.fail("could not finalize rule " + name, (Failure)result);
               } else {
                  state.setValue(result);
                  success = true;
               }
            }
         } else {
            buffer.reset();
         }
         buffer.release();
      }
      return success;
   }

   /**
    * Resolves this rule and the inner expression.
    * @param grammar the grammar to resolve rule references
    */
   @Override
   public void resolve(final Grammar grammar, final Rule parent) {
      initializer = grammar.getFunction(type, initializer.name(), Optional.OBJECT);
      serializer = grammar.getFunction(type, serializer.name(), Optional.OBJECT, String.class);
      finalizer = grammar.getFunction(type, finalizer.name(), Optional.OBJECT);
      simple = !isCustomFunction(initializer) && !isCustomFunction(serializer) && !isCustomFunction(finalizer);
      parser.resolve(grammar, this);
   }

   @Override
   public String toString() {
      return name + " <- " + parser;
   }
}
