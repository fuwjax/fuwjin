package org.fuwjin.pogo.state;

/**
 * Manages the current position of a serial operation.
 */
public class SerialPosition extends AbstractPosition {
   private int start;

   /**
    * Creates a new instance.
    * @param pos the previous position
    * @param start the current buffer position
    * @param line the current line number
    * @param column the current column number
    */
   public SerialPosition(final SerialPosition pos, final int start, final int line, final int column) {
      super(pos, false, line, column);
      this.start = start;
   }

   /**
    * Creates a new initial instance.
    * @param state the pogo state
    */
   public SerialPosition(final SerialState state) {
      super(state);
      start = 0;
   }

   protected void setStart(final int start) {
      this.start = start;
   }

   protected int start() {
      return start;
   }
}
