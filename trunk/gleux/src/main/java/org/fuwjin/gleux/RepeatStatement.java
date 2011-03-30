package org.fuwjin.gleux;

/**
 * Represents a statement that repeats one or more times.
 */
public class RepeatStatement implements Expression {
   private final Expression statement;

   /**
    * Creates a new instance.
    * @param statement the repeated statement
    */
   public RepeatStatement(final Expression statement) {
      this.statement = statement;
   }

   @Override
   public String toString() {
      return "repeat " + statement;
   }

   @Override
   public State transform(final State state) {
      State result = statement.transform(state);
      if(!result.isSuccess()) {
         return result;
      }
      State last = state;
      while(result.isSuccess() && result != last) {
         last = result;
         result = statement.transform(result);
      }
      return last;
   }
}
