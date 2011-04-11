package org.fuwjin.chessur;

public class Pipe extends OutStream implements Expression {
   private final String name;
   private final StringBuilder buffer = new StringBuilder();

   public Pipe(final String name) {
      this.name = name;
   }

   @Override
   protected void append(final Object value) {
      buffer.append(value);
   }

   @Override
   public State transform(final State state) {
      final State result = state.value(buffer.toString());
      return result.assign(name);
   }
}
