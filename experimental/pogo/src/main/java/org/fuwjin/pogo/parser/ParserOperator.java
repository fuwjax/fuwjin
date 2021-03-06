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

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.ParsingExpression;

/**
 * The standard base class for parse operators.
 */
public abstract class ParserOperator implements ParsingExpression {
   /**
    * The target parser for this operaton.
    */
   protected ParsingExpression parser;

   /**
    * Creates a new instance.
    */
   protected ParserOperator() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param parser the operated upon parser
    */
   protected ParserOperator(final ParsingExpression parser) {
      this.parser = parser;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ParserOperator o = (ParserOperator)obj;
         return eq(getClass(), o.getClass()) && eq(parser, o.parser);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), parser);
   }

   @Override
   public void resolve(final Grammar grammar, final String namespace) {
      parser.resolve(grammar, namespace);
   }
}
