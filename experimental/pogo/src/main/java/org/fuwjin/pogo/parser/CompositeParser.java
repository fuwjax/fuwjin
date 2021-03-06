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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.ParsingExpression;

/**
 * A base class for composite parsers.
 */
public abstract class CompositeParser implements ParsingExpression, Iterable<ParsingExpression> {
   private final List<ParsingExpression> parsers = new LinkedList<ParsingExpression>();

   /**
    * Adds a parser to this composite.
    * @param parser the parser to add
    */
   public void add(final ParsingExpression parser) {
      parsers.add(parser);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final CompositeParser o = (CompositeParser)obj;
         return eq(getClass(), o.getClass()) && eq(parsers, o.parsers);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), parsers);
   }

   /**
    * Returns true if this composite parser is composed only of literal parsers.
    * @return true if a literal composite, false otherwise
    */
   protected boolean isLiteral() {
      for(final ParsingExpression parser: parsers) {
         if(!isLiteral(parser)) {
            return false;
         }
      }
      return true;
   }

   /**
    * Returns true if {@code parser} is a literal parser according to this
    * composite.
    * @param parser the parser which may be literal
    * @return true if {@code parser} is literal, false otherwise
    */
   protected abstract boolean isLiteral(ParsingExpression parser);

   @Override
   public Iterator<ParsingExpression> iterator() {
      return parsers.iterator();
   }

   /**
    * Attempts to reduce the structure of this composite. The return value
    * should be used in place of the parser that generated it.
    * @return the reduced parser
    */
   public ParsingExpression reduce() {
      for(int index = 0; index < parsers.size(); index++) {
         final ParsingExpression parser = parsers.get(index);
         if(parser instanceof CompositeParser) {
            parsers.set(index, ((CompositeParser)parser).reduce());
         }
      }
      if(parsers.size() == 1) {
         return parsers.get(0);
      }
      return this;
   }

   @Override
   public void resolve(final Grammar grammar, final String namespace) {
      for(final ParsingExpression parser: parsers) {
         parser.resolve(grammar, namespace);
      }
   }
}
