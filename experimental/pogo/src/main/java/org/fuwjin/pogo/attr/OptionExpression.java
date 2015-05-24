package org.fuwjin.pogo.attr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Matches the first of a set of sub-expressions.
 */
public class OptionExpression implements Expression {
   private final List<Expression> expressions = new ArrayList<Expression>();

   /**
    * Adds an expression to the set of sub-expressons. This expression will be
    * evaluated only after all previous sub-expressions have failed.
    * @param expression the new option
    */
   public void add(final Expression expression) {
      expressions.add(expression);
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      State result = null;
      for(final Expression expression: expressions) {
         result = expression.transition(state, scope);
         if(result.isSuccess()) {
            return result;
         }
      }
      return state.failure(result, "No option found");
   }
}
