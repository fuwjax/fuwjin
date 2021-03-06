/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.chessur.expression;

import java.util.ArrayList;
import java.util.List;
import org.fuwjin.grin.env.Trace;

/**
 * Represents a composite or dynamic literal.
 */
public class CompositeLiteral implements Expression {
   /**
    * Translates the code point to an escaped character if it's necessary
    * @param ch the code point
    * @return the escaped character
    */
   public static String escape(final int ch) {
      switch(ch) {
      case '\'':
         return "\\'";
      case '"':
         return "\\\"";
      case '<':
         return "\\<";
      }
      return Literal.standardEscape(ch);
   }

   private final List<Expression> values = new ArrayList<Expression>();

   /**
    * Append a value.
    * @param value the value
    */
   public void append(final Expression value) {
      values.add(value);
   }

   /**
    * Append a code point.
    * @param codepoint the code point
    */
   public void appendChar(final int codepoint) {
      Literal lit;
      final Expression last = last();
      if(last instanceof Literal) {
         lit = (Literal)last;
      } else {
         lit = new Literal();
         values.add(lit);
      }
      lit.append(codepoint);
   }

   /**
    * Append a code point.
    * @param codepoint the code point as a string
    */
   public void appendString(final String codepoint) {
      appendChar(codepoint.codePointAt(0));
   }

   @Override
   public Object resolve(final Trace trace)
         throws AbortedException, ResolveException {
      final StringBuilder builder = new StringBuilder();
      for(final Expression value: values) {
         final Object result = value.resolve(trace);
         builder.append(result);
      }
      return builder.toString();
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder("\"");
      for(final Expression value: values) {
         builder.append(value);
      }
      return builder.append("\"").toString();
   }

   /**
    * Returns the set of values comprising this literal.
    * @return the value set
    */
   public Iterable<Expression> values() {
      return values;
   }

   protected Expression last() {
      return values.size() == 0 ? null : values.get(values.size() - 1);
   }
}
