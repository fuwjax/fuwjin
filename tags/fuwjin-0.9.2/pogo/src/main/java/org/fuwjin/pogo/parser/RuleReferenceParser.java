/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import static org.fuwjin.pogo.postage.PostageUtils.invoke;
import static org.fuwjin.pogo.postage.PostageUtils.invokeReturn;
import static org.fuwjin.pogo.postage.PostageUtils.isCustomFunction;
import static org.fuwjin.postage.StandardAdaptable.UNSET;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.util.NoSuchElementException;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Rule;
import org.fuwjin.pogo.postage.Doppleganger;
import org.fuwjin.pogo.state.PogoMemo;
import org.fuwjin.pogo.state.PogoPosition;
import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;

/**
 * Matches a rule indirectly and optionally persists the result of the rule.
 */
public class RuleReferenceParser implements Parser {
   private static final String UNKNOWN_RULE = "No rule named %s in grammar"; //$NON-NLS-1$ 
   private String ruleName;
   private Parser rule;
   private Function constructor;
   private Function matcher;
   private Function converter;
   private boolean simple = true;

   /**
    * Creates a new instance.
    */
   RuleReferenceParser() {
      constructor = Doppleganger.create("default", Function.class);
      matcher = Doppleganger.create("default", Function.class);
      converter = Doppleganger.create("default", Function.class);
   }

   /**
    * Creates a new instance.
    * @param ruleName the name of the referred rule
    * @param initializer the initializer for the reference
    * @param serializer the serializer for the reference
    * @param finalizer the finalizer for the reference
    */
   public RuleReferenceParser(final String ruleName, final String initializer, final String serializer,
         final String finalizer) {
      this.ruleName = ruleName;
      constructor = Doppleganger.create(initializer, Function.class);
      matcher = Doppleganger.create(serializer, Function.class);
      converter = Doppleganger.create(finalizer, Function.class);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final RuleReferenceParser o = (RuleReferenceParser)obj;
         return eq(getClass(), o.getClass()) && eq(ruleName, o.ruleName)
               && eq(constructor.name(), o.constructor.name()) && eq(matcher.name(), o.matcher.name())
               && eq(converter.name(), o.converter.name());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), ruleName, constructor, converter);
   }

   @Override
   public boolean parse(final PogoState state) {
      if(simple) {
         final Object parent = state.getValue();
         state.setValue(UNSET);
         final boolean success = rule.parse(state);
         state.setValue(parent);
         return success;
      }
      boolean success = false;
      final Object parent = state.getValue();
      Object result = invoke(constructor, UNSET, parent);
      if(result instanceof Failure) {
         state.fail("could not initialize rule reference " + ruleName, (Failure)result);
      } else {
         final PogoMemo memo = state.getMemo(ruleName, isCustomFunction(matcher));
         if(!memo.isStored()) {
            state.setValue(result);
            final PogoPosition buffer = state.buffer(isCustomFunction(matcher));
            if(rule.parse(state)) {
               memo.store(buffer.toString(), state.getValue());
            } else {
               memo.fail();
               buffer.reset();
            }
            buffer.release();
            state.setValue(parent);
         }
         if(memo.isStored()) {
            result = invokeReturn(matcher, parent, memo.buffer());
            if(result instanceof Failure) {
               state.fail("could not handle rule reference match " + ruleName, (Failure)result);
            } else {
               result = invokeReturn(converter, result, memo.value());
               if(result instanceof Failure) {
                  state.fail("could not finalize rule reference " + ruleName, (Failure)result);
               } else {
                  state.setValue(result);
                  success = true;
               }
            }
         }
         memo.release();
      }
      return success;
   }

   @Override
   public void resolve(final Grammar grammar, final Rule parent) {
      rule = grammar.getRule(ruleName);
      if(rule == null) {
         throw new NoSuchElementException(String.format(UNKNOWN_RULE, ruleName));
      }
      constructor = parent.category().getFunction(Doppleganger.<String> content(constructor));
      matcher = parent.category().getFunction(Doppleganger.<String> content(matcher));
      converter = parent.category().getFunction(Doppleganger.<String> content(converter));
      simple = !isCustomFunction(constructor) && !isCustomFunction(matcher) && !isCustomFunction(converter);
   }

   @Override
   public String toString() {
      return ruleName;
   }
}
