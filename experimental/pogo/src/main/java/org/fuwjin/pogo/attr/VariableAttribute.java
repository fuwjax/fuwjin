package org.fuwjin.pogo.attr;

import java.lang.reflect.Type;
import java.util.Map;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.type.Optional;

/**
 * An attribute whose value is determined by a current state scoped variable.
 */
public class VariableAttribute implements Attribute {
   private final String name;

   /**
    * Creates a new instance.
    * @param name the new instance
    */
   public VariableAttribute(final String name) {
      this.name = name;
   }

   @Override
   public void prepare(final State state, final Map<String, Object> scope) {
      // do nothing
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      // transition from a variable state is poorly defined and should never
      // happen
      return state;
   }

   @Override
   public Type type() {
      return Object.class;
   }

   @Override
   public Object value(final State state, final Map<String, Object> scope) {
      final Object value = scope.get(name);
      if(value == Optional.UNSET) {
         return new Failure("%s has not been set in this scope", name);
      }
      return value;
   }
}
