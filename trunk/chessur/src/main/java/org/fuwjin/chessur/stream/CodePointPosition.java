package org.fuwjin.chessur.stream;

import org.fuwjin.util.UnicodeUtils;

/**
 * A codepoint based position.
 */
public class CodePointPosition implements Position {
   /**
    * The end of file marker.
    */
   public static final int EOF = -1;
   /**
    * The start of file marker.
    */
   public static final int SOF = -2;
   private final int pos;
   private final int line;
   private final int column;
   private final int codePoint;

   /**
    * Creates a new instance.
    */
   public CodePointPosition() {
      pos = 0;
      line = 1;
      column = 0;
      codePoint = SOF;
   }

   private CodePointPosition(final int pos, final int line, final int column, final int codePoint) {
      this.pos = pos;
      this.line = line;
      this.column = column;
      this.codePoint = codePoint;
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
      return codePoint != EOF && codePoint != SOF;
   }

   @Override
   public int line() {
      return line;
   }

   @Override
   public String toString() {
      switch(codePoint) {
      case SOF:
         return "[" + line + "," + column + "] SOF";
      case EOF:
         return "[" + line + "," + column + "] EOF";
      case '\n':
         return "[" + line + "," + column + "] '\\n'";
      case '\r':
         return "[" + line + "," + column + "] '\\r'";
      case '\t':
         return "[" + line + "," + column + "] '\\t'";
      default:
         return "[" + line + "," + column + "] '" + valueString() + "'";
      }
   }

   @Override
   public Integer value() {
      return codePoint;
   }

   @Override
   public String valueString() {
      return UnicodeUtils.toString(codePoint);
   }

   protected CodePointPosition newPosition(final int next, final int size) {
      if(codePoint == '\n') {
         return new CodePointPosition(pos + size, line + 1, 1, next);
      }
      return new CodePointPosition(pos + size, line, column + 1, next);
   }
}
