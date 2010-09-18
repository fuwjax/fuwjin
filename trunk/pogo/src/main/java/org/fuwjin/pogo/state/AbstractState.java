package org.fuwjin.pogo.state;

import org.fuwjin.postage.Failure;

public abstract class AbstractState implements PogoState {
   private Object value;
   private AbstractPosition current;
   private int marks;
   private int memos;
   private final ParseFailure failure = new ParseFailure();

   @Override
   public AbstractPosition current() {
      return current;
   }

   @Override
   public ParseException exception() {
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
      failure.failStack(level, name, pos);
   }

   @Override
   public ParseMemo getMemo(final String name, final boolean needsBuffer) {
      memos++;
      ParseMemo memo = current.getMemo(name, needsBuffer);
      if(memo == null) {
         memo = new ParseMemo(name, this);
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
