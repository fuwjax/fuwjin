package org.fuwjin.gleux;

/**
 * Represents a statement that always succeeds.
 */
public class CouldStatement implements Expression {
   private final Expression statement;

   /**
    * Creates a new instance.
    * @param statement the possible statement
    */
   public CouldStatement(final Expression statement) {
      this.statement = statement;
   }

   @Override
   public String toString() {
      return "could " + statement;
   }

   @Override
   public State transform(final State state) {
      final State result = statement.transform(state);
      if(result.isSuccess()) {
         return result;
      }
      return state;
   }
}
