package org.fuwjin.gleux;

/**
 * Represents an assignment.
 */
public class Assignment implements Expression {
   private final String name;
   private final Expression value;

   /**
    * Creates a new instance.
    * @param name the variable name
    * @param value the new value
    */
   public Assignment(final String name, final Expression value) {
      this.name = name;
      this.value = value;
   }

   @Override
   public String toString() {
      return name + " = " + value;
   }

   @Override
   public State transform(final State state) {
      final State result = value.transform(state);
      if(!result.isSuccess()) {
         return state.failure(result, "could not assign to %s", name);
      }
      return result.assign(name);
   }
}
