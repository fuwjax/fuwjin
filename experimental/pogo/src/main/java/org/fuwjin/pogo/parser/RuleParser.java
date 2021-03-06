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

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.util.LinkedList;

import org.fuwjin.pogo.Attribute;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.ParsingExpression;

/**
 * A grammar rule.
 */
public class RuleParser extends Parser {
   private String name;
   private ParsingExpression parser;
   private ParsingExpression attributed;
   private final String namespace;
   private final LinkedList<Attribute> attributes = new LinkedList<Attribute>();

   /**
    * Creates a new instance.
    */
   RuleParser() {
      namespace = "default";
   }

   /**
    * Creates a new instance.
    * @param name the rule name
    */
   RuleParser(final String name) {
      this();
      this.name = name;
   }

   /**
    * Creates a new instance.
    * @param name the name
    * @param namespace the bound namespace
    */
   public RuleParser(final String name, final String namespace) {
      this.name = name;
      this.namespace = namespace;
   }

   public RuleParser add(final Attribute attribute) {
      attributes.add(attribute);
      return this;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final RuleParser o = (RuleParser)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name) && eq(namespace, o.namespace) && eq(parser, o.parser)
               && eq(attributes, o.attributes);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public ParsingExpression expression() {
      return attributed;
   }

   public RuleParser expression(final ParsingExpression expression) {
      parser = expression;
      return this;
   }

   @Override
   public int hashCode() {
      return hash(getClass(), name, namespace, parser);
   }

   /**
    * Returns the name.
    * @return the name
    */
   public String name() {
      return name;
   }

   /**
    * Resolves this rule and the inner expression.
    * @param grammar the grammar to resolve rule references
    */
   public void resolve(final Grammar grammar, final String ignore) {
      attributed = parser;
      for(final Attribute attribute: attributes) {
         attributed = attribute.decorate(attributed);
      }
      attributed.resolve(grammar, namespace);
   }

   @Override
   public String toString() {
      return name + "=" + namespace + attributes + " <- " + parser;
   }
}
