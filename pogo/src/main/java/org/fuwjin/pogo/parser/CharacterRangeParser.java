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
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.state.PogoState;

/**
 * Matches a single character against a range. The range is from start to end,
 * inclusive.
 */
public class CharacterRangeParser implements Parser {
   private int start;
   private int end;

   @Override
   public boolean equals(final Object obj) {
      try {
         final CharacterRangeParser o = (CharacterRangeParser)obj;
         return eq(getClass(), o.getClass()) && eq(start, o.start) && eq(end, o.end);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   /**
    * Returns the end of the range.
    * @return the end of the range
    */
   public char getEnd() {
      return (char)end;
   }

   /**
    * Returns the start of the range.
    * @return the start of the range
    */
   public char getStart() {
      return (char)start;
   }

   @Override
   public int hashCode() {
      return hash(getClass(), start, end);
   }

   @Override
   public boolean parse(final PogoState state) {
      return state.advance(start, end);
   }

   @Override
   public void resolve(final Grammar grammar, final String namespace) {
      // do nothing
   }

   /**
    * Sets the end of the range.
    * @param end the end of the range
    */
   public void setEnd(final int end) {
      this.end = end;
   }

   /**
    * Sets the maximum value for this range.
    * @param end the largest acceptable character value to match
    */
   public void setEnd(final String end) {
      this.end = end.charAt(0);
   }

   /**
    * Sets the minimum value for this range.
    * @param start the smallest acceptable character value to match
    */
   public void setStart(final int start) {
      this.start = start;
   }

   /**
    * Sets the minimum value for this range.
    * @param start the smallest acceptable character value to match
    */
   public void setStart(final String start) {
      this.start = start.charAt(0);
   }

   @Override
   public String toString() {
      return '[' + new String(Character.toChars(start)) + '-' + new String(Character.toChars(end)) + ']';
   }
}
