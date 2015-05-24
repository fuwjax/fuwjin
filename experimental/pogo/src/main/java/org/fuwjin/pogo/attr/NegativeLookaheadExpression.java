package org.fuwjin.pogo.attr;

import java.util.Map;

/**
 * Transform the state against a sub-expression, reversing the success/failure
 * result. In either case, the state is unchanged.
 */
public class NegativeLookaheadExpression implements Expression {
   private final Expression expression;

   /**
    * Creates a new instance.
    * @param expression the sub-expression
    */
   public NegativeLookaheadExpression(final Expression expression) {
      this.expression = expression;
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      final State result = expression.transition(state, scope);
      if(result.isSuccess()) {
         return state.failure("Unexpected match");
      }
      return state;
   }
}
