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
package org.fuwjin.pogo;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.fuwjin.pogo.parser.RuleParser;
import org.fuwjin.pogo.postage.PogoCategory;
import org.fuwjin.pogo.postage.PostageUtils;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.category.ReflectionCategory;
import org.fuwjin.postage.type.Optional;

/**
 * The parser generated by PogoGrammar.
 */
public class Grammar extends Parser implements Iterable<RuleParser> {
   /**
    * Creates a new Pogo parser from the Pogo formatted input.
    * @param stream the input
    * @return the new parser
    * @throws PogoException if the parse fails
    */
   public static Grammar readGrammar(final CodePointStream stream) throws PogoException {
      return (Grammar)PogoGrammar.staticPogoGrammar().parse(stream);
   }

   /**
    * Creates a new Pogo parser from the Pogo formatted input with the custom
    * postage.
    * @param stream the input
    * @param postage the custom postage
    * @return the new grammar
    * @throws PogoException if the parse fails
    */
   public static Grammar readGrammar(final CodePointStream stream, final Postage postage) throws PogoException {
      return (Grammar)PogoGrammar.staticPogoGrammar().parse(stream, postage);
   }

   private final Map<String, RuleParser> rules = new LinkedHashMap<String, RuleParser>();
   private final Postage postage;
   private RuleParser primary;

   /**
    * Creates a new instance.
    */
   public Grammar() {
      this(new Postage(new ReflectionCategory(), new PogoCategory()));
   }

   /**
    * Creates a new instance.
    * @param postage the custom postage.
    */
   public Grammar(final Postage postage) {
      this.postage = postage;
   }

   /**
    * Adds a new rule to the grammar.
    * @param rule the new rule
    */
   protected void add(final RuleParser rule) {
      if(rules.isEmpty()) {
         primary = rule;
      }
      rules.put(rule.name(), rule);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final Grammar o = (Grammar)obj;
         return eq(getClass(), o.getClass()) && eq(rules, o.rules);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   protected ParsingExpression expression() {
      return primary.expression();
   }

   /**
    * Retrieves a rule from the grammar by name.
    * @param name the name of the rule
    * @return the rule by that name, or null if there is no such rule
    */
   public RuleParser get(final String name) {
      final RuleParser rule = rules.get(name);
      if(rule == null) {
         throw new IllegalArgumentException(name);
      }
      return rule;
   }

   /**
    * Returns the named function.
    * @param category the function category prefix
    * @param name the name of the function
    * @param parameters the function parameters
    * @return the function
    */
   public Function getFunction(final String category, final String name, final Type... parameters) {
      final String qualifiedName = name.indexOf('.') > -1 ? name : category + "." + name;
      return postage.getFunction(qualifiedName).withSignature(Optional.OBJECT, parameters).filter(
            PostageUtils.POGO_FILTER);
   }

   /**
    * Returns the named rule.
    * @param name the rule name
    * @return the rule
    */
   public RuleParser getRule(final String name) {
      return get(name);
   }

   @Override
   public int hashCode() {
      return hash(getClass(), rules);
   }

   @Override
   public Iterator<RuleParser> iterator() {
      return rules.values().iterator();
   }

   /**
    * Resolves the rules in this grammar.
    * @return this grammar
    */
   protected Grammar resolve() {
      for(final RuleParser rule: rules.values()) {
         rule.resolve(this, null);
      }
      return this;
   }

   /**
    * Returns the generated code corresponding to this grammar.
    * @param qualifiedName the package/classname of the generated class
    * @return the generated code
    * @throws PogoException if there was a serialization error
    */
   public String toCode(final String qualifiedName) throws PogoException {
      return PredefinedGrammar.grammarToJava(qualifiedName, this);
   }

   /**
    * Returns the pogo grammar for this grammar.
    * @return the generated pogo grammar
    * @throws PogoException if there was a serialization error
    */
   public String toPogo() throws PogoException {
      return PredefinedGrammar.grammarToPogo(this);
   }

   @Override
   public String toString() {
      return "Grammar " + super.toString();
   }
}