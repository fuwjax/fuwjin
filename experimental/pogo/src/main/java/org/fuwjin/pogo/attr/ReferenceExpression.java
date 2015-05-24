package org.fuwjin.pogo.attr;

import java.util.Map;

import org.fuwjin.postage.type.Optional;

/**
 * Handles the transition between rules.
 */
public class ReferenceExpression implements Expression {
   private final Attribute attribute;
   private final Rule rule;

   /**
    * Creates a new instance.
    * @param rule the referenced rule
    */
   public ReferenceExpression(final Rule rule) {
      this.rule = rule;
      attribute = null;
   }

   public ReferenceExpression(final Rule rule, final Attribute attribute) {
      this.rule = rule;
      this.attribute = attribute;
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      attribute.prepare(state, scope);
      final Map<String, Object> sub = state.newScope(scope);
      sub.put("this", Optional.UNSET);
      State result = rule.expression().transition(state, sub);
      if(!result.isSuccess()) {
         return state.failure(result, "in rule %s", rule.name());
      }
      if(attribute != null) {
         final Object thisVar = sub.get("this");
         scope.put("result", thisVar);
         result = attribute.transition(result, scope);
         scope.put("result", Optional.UNSET);
         if(!result.isSuccess()) {
            return state.failure(result, "in result attribute for rule %s", rule.name());
         }
      }
      return result;
   }
}
