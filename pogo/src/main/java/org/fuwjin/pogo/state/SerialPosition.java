package org.fuwjin.pogo.state;

public class SerialPosition extends AbstractPosition {
   private final int start;

   public SerialPosition(final SerialPosition pos, final int start, final int line, final int column) {
      super(pos, false, line, column);
      this.start = start;
   }

   public SerialPosition(final SerialState state) {
      super(state);
      start = 0;
   }

   protected int start() {
      return start;
   }
}
