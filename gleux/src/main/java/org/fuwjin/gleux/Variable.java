package org.fuwjin.gleux;

/**
 * Represents a variable in the current scope.
 */
public class Variable implements Expression {
   /**
    * Represents a match within a specification.
    */
   public static final Variable MATCH = new Variable("match") {
      @Override
      public State transform(final State state) {
         return state.substring();
      }
   };
   /**
    * Represents the next character.
    */
   public static final Variable NEXT = new Variable("next") {
      @Override
      public State transform(final State state) {
         if(state.current() == InStream.EOF) {
            return state.failure("unexpected EOF");
         }
         return state.value(state.current());
      }
   };
   private final String name;

   /**
    * Creates a new instance.
    * @param name the variable name
    */
   public Variable(final String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return name;
   }

   @Override
   public State transform(final State state) {
      return state.retrieve(name);
   }
}