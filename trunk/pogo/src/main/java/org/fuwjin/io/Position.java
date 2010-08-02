package org.fuwjin.io;

public class Position {
   private final int position;
   private final int column;
   private final int line;

   public Position(final int position, final int line, final int column) {
      this.position = position;
      this.line = line;
      this.column = column;
   }

   public int column() {
      return column;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final Position o = (Position)obj;
         return o.position == position;
      } catch(final RuntimeException e) {
         return false;
      }
   }

   public int line() {
      return line;
   }

   public int position() {
      return position;
   }
}
