package org.fuwjin.pogo.state;

public class ParsePosition extends AbstractPosition {
   private static int nextColumn(final ParsePosition previous) {
      return previous.ch == '\n' ? 1 : previous.column() + 1;
   }

   private static int nextLine(final ParsePosition previous) {
      return previous.ch == '\n' ? previous.line() + 1 : previous.line();
   }

   private final int ch;
   private int start;

   public ParsePosition(final ParsePosition previous, final boolean shouldBufferNext, final int start, final int ch) {
      super(previous, shouldBufferNext, nextLine(previous), nextColumn(previous));
      this.start = start;
      this.ch = ch;
   }

   public ParsePosition(final ParseState state, final int ch) {
      super(state);
      this.ch = ch;
      start = 0;
   }

   protected int codePoint() {
      return ch;
   }

   public void setStart(final int length) {
      start = length;
   }

   public int start() {
      return start;
   }
}