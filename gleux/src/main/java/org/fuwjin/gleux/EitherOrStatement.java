package org.fuwjin.gleux;

import static java.util.Collections.unmodifiableCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set of ordered options.
 */
public class EitherOrStatement implements Expression {
   private final List<Expression> statements = new ArrayList<Expression>();

   /**
    * Creates a new instance.
    * @param statement the "either" statement
    */
   public EitherOrStatement(final Expression statement) {
      statements.add(statement);
   }

   /**
    * Adds a statement to the block.
    * @param statement the next option
    * @return this statement
    */
   public EitherOrStatement or(final Expression statement) {
      statements.add(statement);
      return this;
   }

   /**
    * Returns the list of options.
    * @return the options
    */
   public Iterable<Expression> statements() {
      return unmodifiableCollection(statements);
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder("either ");
      for(final Expression statement: statements) {
         builder.append("\nor ").append(statement);
      }
      return builder.toString();
   }

   @Override
   public State transform(final State state) {
      State failure = null;
      for(final Expression statement: statements) {
         final State result = statement.transform(state);
         if(result.isSuccess()) {
            return result;
         }
         failure = result;
      }
      return state.failure(failure, "No valid option");
   }
}
