package org.fuwjin.pogo.attr;

import java.util.Map;

import org.fuwjin.util.CodePointSet;

/**
 * An expression which matches a set of characters.
 */
public class CharSetExpression implements Expression {
   /**
    * Matches any character except EOF.
    */
   public static final Expression ANY_CHAR = new Expression() {
      @Override
      public State transition(final State state, final Map<String, Object> scope) {
         return state.next();
      }
   };
   private final CodePointSet set = new CodePointSet();

   /**
    * Adds a character to the set.
    * @param codePoint the character to include
    */
   public void addChar(final int codePoint) {
      set.unionRange(codePoint, codePoint);
   }

   /**
    * Adds a character range to the set. The range includes both start and end.
    * It is undefined for start to be greater than end, or for either number to
    * be negative.
    * @param start the low codepoint in the range
    * @param end the high codepoint in the range
    */
   public void addRange(final int start, final int end) {
      set.unionRange(start, end);
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      final int ch = state.codePoint();
      if(set.contains(state.codePoint())) {
         return state.next();
      }
      return state.failure("Could not find '%c' in [%s]", ch, set);
   }
}
