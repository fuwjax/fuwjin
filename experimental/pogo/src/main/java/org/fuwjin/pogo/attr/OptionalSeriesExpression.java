package org.fuwjin.pogo.attr;

import java.util.Map;

/**
 * An expression which may match zero or more copies of a sub-expression. This
 * expression always successfully transforms.
 */
public class OptionalSeriesExpression implements Expression {
   private final Expression expression;

   /**
    * Creates a new instance.
    * @param expression the optionally repeated expression
    */
   public OptionalSeriesExpression(final Expression expression) {
      this.expression = expression;
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      State s = state;
      State result = expression.transition(s, scope);
      while(result.isSuccess()) {
         s = result;
         result = expression.transition(s, scope);
      }
      return s;
   }
}
