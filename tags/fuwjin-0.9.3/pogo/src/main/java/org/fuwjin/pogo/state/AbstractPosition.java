package org.fuwjin.pogo.state;

import java.util.LinkedList;
import java.util.List;

/**
 * The standard position implementation.
 */
public abstract class AbstractPosition implements PogoPosition {
   private final AbstractState state;
   private final int index;
   private final int line;
   private final int column;
   private final List<PogoMemo> memos;
   private PogoPosition next;

   /**
    * Creates a new instance from a previous instance.
    * @param previous the previous instance
    * @param shouldBufferNext true if the previous instance should point to this
    *        new instance
    * @param line the new line number
    * @param column the new column number
    */
   public AbstractPosition(final AbstractPosition previous, final boolean shouldBufferNext, final int line,
         final int column) {
      if(shouldBufferNext) {
         previous.next = this;
      } else {
         previous.next = PogoPosition.NULL;
      }
      index = previous.index + 1;
      memos = new LinkedList<PogoMemo>();
      state = previous.state;
      this.line = line;
      this.column = column;
   }

   /**
    * Creates a new initial instance.
    * @param state the state managing the positions
    */
   public AbstractPosition(final AbstractState state) {
      this.state = state;
      memos = new LinkedList<PogoMemo>();
      index = 0;
      line = 1;
      column = 1;
   }

   protected void clearNext() {
      next = PogoPosition.NULL;
   }

   protected int column() {
      return column;
   }

   protected PogoMemo getMemo(final String name, final boolean needsBuffer) {
      for(final PogoMemo memo: memos) {
         if(memo.name().equals(name)) {
            if(!needsBuffer || memo.buffer() != null) {
               return memo;
            }
         }
      }
      return null;
   }

   protected int index() {
      return index;
   }

   protected int line() {
      return line;
   }

   protected PogoPosition next() {
      return next;
   }

   @Override
   public void release() {
      state.release();
   }

   @Override
   public void reset() {
      state.set(this);
   }

   protected void setMemo(final PogoMemo memo) {
      memos.add(memo);
   }

   @Override
   public String toString() {
      return "[" + line + "," + column + "]";
   }
}