package org.fuwjin.pogo.state;

/**
 * Manages a stream position during a parse operation.
 */
public class ParsePosition extends AbstractPosition {
   private static int nextColumn(final ParsePosition previous) {
      return previous.ch == '\n' ? 1 : previous.column() + 1;
   }

   private static int nextLine(final ParsePosition previous) {
      return previous.ch == '\n' ? previous.line() + 1 : previous.line();
   }

   private final int ch;
   private int start;

   /**
    * Creates a new instance.
    * @param previous the previous position
    * @param shouldBufferNext true if the previous position should point to this
    *        position
    * @param start the buffer position corresponding to this position
    * @param ch the character in the stream at this position
    */
   public ParsePosition(final ParsePosition previous, final boolean shouldBufferNext, final int start, final int ch) {
      super(previous, shouldBufferNext, nextLine(previous), nextColumn(previous));
      this.start = start;
      this.ch = ch;
   }

   /**
    * Creates a new initial instance.
    * @param state the parse state
    * @param ch the first character in the stream
    */
   public ParsePosition(final ParseState state, final int ch) {
      super(state);
      this.ch = ch;
      start = 0;
   }

   protected int codePoint() {
      return ch;
   }

   /**
    * Sets the buffer position corresponding to this position.
    * @param start the buffer position
    */
   public void setStart(final int start) {
      this.start = start;
   }

   /**
    * Returns the buffer position corresponding to this position.
    * @return the buffer position
    */
   public int start() {
      return start;
   }
}