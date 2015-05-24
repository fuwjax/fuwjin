package org.fuwjin.pogo.attr;

import java.util.Map;

/**
 * An expression that may or may not match the next input. Either way, it
 * returns a success.
 */
public class OptionalExpression implements Expression {
   private final Expression expression;

   /**
    * Creates a new instance.
    * @param expression the sub-expression
    */
   public OptionalExpression(final Expression expression) {
      this.expression = expression;
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      final State result = expression.transition(state, scope);
      if(result.isSuccess()) {
         return result;
      }
      return state;
   }
}
