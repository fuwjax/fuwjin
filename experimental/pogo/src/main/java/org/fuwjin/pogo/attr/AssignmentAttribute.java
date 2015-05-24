package org.fuwjin.pogo.attr;

import java.util.Map;

import org.fuwjin.postage.Failure;

/**
 * Assigns the value of another attribute to a local variable.
 */
public class AssignmentAttribute implements Attribute {
   private final Attribute attribute;
   private final String name;

   /**
    * Creates a new instance.
    * @param name the variable name
    * @param attribute the value attribute
    */
   public AssignmentAttribute(final String name, final Attribute attribute) {
      this.name = name;
      this.attribute = attribute;
   }

   @Override
   public void prepare(final State state, final Map<String, Object> scope) {
      attribute.prepare(state, scope);
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      final Object value = attribute.value(state, scope);
      if(value instanceof Failure) {
         return state.failure(value, "Failed while assigning to %s", name);
      }
      scope.put(name, value);
      return state;
   }

   @Override
   public Class<?> type() {
      throw new UnsupportedOperationException("An assignment attribute has no value");
   }

   @Override
   public Object value(final State state, final Map<String, Object> scope) {
      throw new UnsupportedOperationException("An assignment attribute has no value");
   }
}
