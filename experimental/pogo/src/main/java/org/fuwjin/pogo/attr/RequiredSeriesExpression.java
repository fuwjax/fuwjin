package org.fuwjin.pogo.attr;

import java.util.Map;

/**
 * An expression which may match one or more copies of a sub-expression.
 */
public class RequiredSeriesExpression implements Expression {
   private final Expression expression;

   /**
    * Creates a new instance.
    * @param expression the repeated expression
    */
   public RequiredSeriesExpression(final Expression expression) {
      this.expression = expression;
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      State s = state;
      State result = expression.transition(s, scope);
      if(!result.isSuccess()) {
         return result;
      }
      while(result.isSuccess()) {
         s = result;
         result = expression.transition(s, scope);
      }
      return s;
   }
}
