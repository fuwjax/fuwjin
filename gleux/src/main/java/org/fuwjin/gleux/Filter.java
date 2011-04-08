package org.fuwjin.gleux;

import org.fuwjin.util.CodePointSet;
import org.fuwjin.util.CodePointSet.Range;

/**
 * Represents a filter for accept.
 */
public class Filter {
   /**
    * Translates the code point to an escaped character if it's necessary
    * @param ch the code point
    * @return the escaped character
    */
   public static String escape(final int ch) {
      switch(ch) {
         case ',':
            return "\\,";
         case ' ':
            return "\\ ";
      }
      return Literal.standardEscape(ch);
   }

   private final CodePointSet set = new CodePointSet();

   /**
    * Adds a character to the filter.
    * @param ch the codepoint to filter
    * @return this filter
    */
   public Filter addChar(final int ch) {
      set.unionRange(ch, ch);
      return this;
   }

   /**
    * Adds a character range to the filter.
    * @param start the first codepoint to filter
    * @param end the last codepoint to filter
    * @return this filter
    */
   public Filter addRange(final int start, final int end) {
      set.unionRange(start, end);
      return this;
   }

   /**
    * Tests for filter containment.
    * @param test the test codepoint
    * @return true if the test codepoint is matched by this filter, false
    *         otherwise
    */
   public boolean allow(final int test) {
      return set.contains(test);
   }

   /**
    * Returns the set of ranges.
    * @return the ranges
    */
   public Iterable<Range> ranges() {
      return set.ranges();
   }

   @Override
   public String toString() {
      return set.toString();
   }
}
