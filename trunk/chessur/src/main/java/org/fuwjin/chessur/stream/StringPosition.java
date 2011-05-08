package org.fuwjin.chessur.stream;

import org.fuwjin.util.UnicodeUtils;

public class StringPosition implements Position {
   private final int pos;
   private final int line;
   private final int column;
   private final String string;
   private final Object value;

   public StringPosition() {
      pos = 0;
      line = 1;
      column = 0;
      value = null;
      string = null;
   }

   private StringPosition(final int pos, final int line, final int column, final String string, final Object value) {
      this.pos = pos;
      this.line = line;
      this.column = column;
      this.string = string;
      this.value = value;
   }

   @Override
   public int column() {
      return column;
   }

   @Override
   public int index() {
      return pos;
   }

   @Override
   public boolean isValid() {
      return string != null;
   }

   @Override
   public int line() {
      return line;
   }

   public StringPosition newPosition(final Object v) {
      final String next = String.valueOf(v);
      int index = next.lastIndexOf('\n');
      int newLine = line;
      int newCol;
      if(index >= 0) {
         newCol = next.length() - index - 1;
         while(index >= 0) {
            newLine++;
            index = next.lastIndexOf('\n', index - 1);
         }
      } else {
         newCol = column + next.length();
      }
      return new StringPosition(pos + next.length(), newLine, newCol, next, v);
   }

   @Override
   public String toString() {
      if(isValid()) {
         return "[" + line + "," + column + "] '" + lastChar() + "'";
      }
      return "[" + line + "," + column + "] SOF";
   }

   @Override
   public Object value() {
      return value;
   }

   @Override
   public String valueString() {
      return string;
   }

   private String lastChar() {
      return UnicodeUtils.toString(valueString().codePointBefore(valueString().length()));
   }
}
