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

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.fuwjin.pogo.Attribute;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.LiteratePogo;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.state.PogoMemo;
import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.type.Optional;

/**
 * Matches a rule indirectly and optionally persists the result of the rule.
 */
public class RuleReferenceParser implements Parser {
   public class MemoParser implements Parser {
      @Override
      public boolean parse(final PogoState state) {
         if(!isInit) {
            state.setValue(Optional.UNSET);
         }
         final PogoMemo memo = state.getMemo(ruleName);
         if(!memo.isStored()) {
            if(rule.parse(state)) {
               memo.store();
            } else {
               memo.fail();
            }
         }
         return memo.isStored();
      }

      @Override
      public void resolve(final Grammar grammar, final String namespace) {
         // ignore
      }
   }

   private static final String UNKNOWN_RULE = "No rule named %s in grammar"; //$NON-NLS-1$ 
   private String ruleName;
   private Parser rule;
   private Function constructor;
   private Function matcher;
   private Function converter;
   private Parser attributed;
   private final LinkedList<Attribute> attributes = new LinkedList<Attribute>();
   private boolean isReturn = false;
   private boolean isInit = false;

   /**
    * Creates a new instance.
    */
   RuleReferenceParser() {
   }

   /**
    * Creates a new instance.
    * @param ruleName the name of the referred rule
    */
   public RuleReferenceParser(final String ruleName) {
      this.ruleName = ruleName;
   }

   public RuleReferenceParser add(final Attribute attribute) {
      if(attribute == ReferenceMatchAttribute.RETURN || attribute == ReferenceResultAttribute.RETURN) {
         isReturn = true;
      }
      if(attribute instanceof ReferenceInitAttribute) {
         isInit = true;
      }
      attributes.add(attribute);
      return this;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final RuleReferenceParser o = (RuleReferenceParser)obj;
         return eq(getClass(), o.getClass()) && eq(ruleName, o.ruleName) && eq(attributes, o.attributes);
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
      final Object parent = state.getValue();
      final boolean success = attributed.parse(state);
      if(!success || !isReturn) {
         state.setValue(parent);
      }
      return success;
   }

   @Override
   public void resolve(final Grammar grammar, final String namespace) {
      rule = grammar.getRule(ruleName);
      if(rule == null) {
         throw new NoSuchElementException(String.format(UNKNOWN_RULE, ruleName));
      }
      if(constructor != null && isCustomFunction(constructor)) {
         add(new ReferenceInitAttribute(constructor.name()));
      }
      if(matcher != null && isCustomFunction(matcher)) {
         add(LiteratePogo.matchRef(matcher.name()));
      }
      if(converter != null && isCustomFunction(converter)) {
         add(LiteratePogo.resultRef(converter.name()));
      }
      if(attributes.isEmpty()) {
         attributed = rule;
      } else {
         attributed = new MemoParser();
         for(final Attribute attribute: attributes) {
            attributed = attribute.decorate(attributed);
         }
         attributed.resolve(grammar, namespace);
      }
   }

   @Override
   public String toString() {
      return ruleName + attributes;
   }
}
