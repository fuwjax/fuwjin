package org.fuwjin.pogo.state;

import org.fuwjin.pogo.PogoException;
import org.fuwjin.postage.Failure;

/**
 * The standard state implementation.
 */
public abstract class AbstractState implements PogoState {
   private Object value;
   private AbstractPosition current;
   private int marks;
   private int memos;
   private final PogoFailure failure = new PogoFailure();

   @Override
   public AbstractPosition current() {
      return current;
   }

   @Override
   public PogoException exception() {
      return failure.exception();
   }

   protected void fail(final int start, final int end) {
      failure.fail(current, start, end);
   }

   @Override
   public void fail(final String string, final Failure cause) {
      failure.fail(current, string, cause);
   }

   protected void failStack(final int level, final String name, final AbstractPosition pos) {
      failure.failStack(pos, name, level);
   }

   @Override
   public PogoMemo getMemo(final String name, final boolean needsBuffer) {
      memos++;
      PogoMemo memo = current.getMemo(name, needsBuffer);
      if(memo == null) {
         memo = new PogoMemo(name, this);
      } else {
         current = memo.end();
      }
      memo.setLevel(memos);
      return memo;
   }

   @Override
   public Object getValue() {
      return value;
   }

   @Override
   public boolean isAfter(final PogoPosition mark) {
      return current.index() > ((AbstractPosition)mark).index();
   }

   @Override
   public AbstractPosition mark() {
      marks++;
      return current;
   }

   protected void release() {
      marks--;
   }

   protected void releaseMemo() {
      memos--;
   }

   protected void set(final AbstractPosition pos) {
      current = pos;
   }

   @Override
   public void setValue(final Object object) {
      value = object;
   }

   protected boolean shouldBufferNext() {
      return marks > 0;
   }
}
