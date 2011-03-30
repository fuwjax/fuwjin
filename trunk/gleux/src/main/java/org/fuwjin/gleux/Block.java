package org.fuwjin.gleux;

import static java.util.Collections.unmodifiableCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sequence of statements.
 */
public class Block implements Expression {
   private final List<Expression> statements = new ArrayList<Expression>();

   /**
    * Adds a statement to the block.
    * @param statement the next statement
    */
   public void add(final Expression statement) {
      statements.add(statement);
   }

   /**
    * Returns the list of statements.
    * @return the list of statements
    */
   public Iterable<Expression> statements() {
      return unmodifiableCollection(statements);
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder("{");
      for(final Expression statement: statements) {
         builder.append("\n  ").append(statement);
      }
      return builder.append("\n}").toString();
   }

   @Override
   public State transform(final State state) {
      State s = state;
      for(final Expression statement: statements) {
         s = statement.transform(s);
         if(!s.isSuccess()) {
            return state.failure(s, "Error in sequence");
         }
      }
      return s;
   }
}
