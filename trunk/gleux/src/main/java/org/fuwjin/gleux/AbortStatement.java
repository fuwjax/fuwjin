package org.fuwjin.gleux;

/**
 * Represents an unrecoverable failure.
 */
public class AbortStatement implements Expression {
   private final Expression value;

   /**
    * Creates a new instance.
    * @param value the failure message
    */
   public AbortStatement(final Expression value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return "abort " + value;
   }

   @Override
   public State transform(final State state) {
      throw new RuntimeException(String.valueOf(value.transform(state).value()) + " " + state);
   }
}
