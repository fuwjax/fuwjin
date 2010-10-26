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

import org.fuwjin.pogo.Attribute;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Rule;
import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.postage.Function;

/**
 * A grammar rule.
 */
public class RuleParser implements Rule {
   private String name;
   private Parser parser;
   private Parser attributed;
   private final String namespace;
   private Function initializer;
   private Function serializer;
   private Function finalizer;
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

   public RuleParser expression(final Parser expression) {
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
   @Override
   public String name() {
      return name;
   }

   @Override
   public boolean parse(final PogoState state) {
      return attributed.parse(state);
   }

   /**
    * Resolves this rule and the inner expression.
    * @param grammar the grammar to resolve rule references
    */
   @Override
   public void resolve(final Grammar grammar, final String ignore) {
      if(initializer != null && isCustomFunction(initializer)) {
         add(new RuleInitAttribute(initializer.name()));
      }
      if(serializer != null && isCustomFunction(serializer)) {
         add(new RuleMatchAttribute(serializer.name()));
      }
      if(finalizer != null && isCustomFunction(finalizer)) {
         add(new RuleResultAttribute(finalizer.name()));
      }
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
