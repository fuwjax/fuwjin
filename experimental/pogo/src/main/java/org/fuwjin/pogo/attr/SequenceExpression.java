package org.fuwjin.pogo.attr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Matches a list of sub-expressions.
 */
public class SequenceExpression implements Expression {
   private final List<Expression> expressions = new ArrayList<Expression>();

   /**
    * Appends an expression to the list of sub-expressions.
    * @param expression the new sub-expression
    */
   public void add(final Expression expression) {
      expressions.add(expression);
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      State s = state;
      for(final Expression expression: expressions) {
         s = expression.transition(s, scope);
         if(!s.isSuccess()) {
            break;
         }
      }
      return s;
   }
}
