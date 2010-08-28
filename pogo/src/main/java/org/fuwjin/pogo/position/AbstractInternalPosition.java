package org.fuwjin.pogo.position;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.pogo.BufferedPosition;
import org.fuwjin.pogo.Memo;
import org.fuwjin.pogo.PogoException;
import org.fuwjin.pogo.Position;
import org.fuwjin.postage.Adaptable;
import org.fuwjin.postage.Failure;
import org.fuwjin.postage.StandardAdaptable;

public abstract class AbstractInternalPosition implements InternalPosition, BufferedPosition {
   public static final Object NO_MATCH = new Object() {
      @Override
      public String toString() {
         throw new UnsupportedOperationException();
      }
   };
   private int line;
   private int column;
   private final PogoState state;
   private final List<Memo> memos = new LinkedList<Memo>();

   protected AbstractInternalPosition() {
      line = 1;
      column = 0;
      state = new PogoState(this);
   }

   protected AbstractInternalPosition(final AbstractInternalPosition prev) {
      line = prev.line;
      column = prev.column;
      state = prev.state;
   }

   protected void accept(final int ch) {
      if(ch == '\n') {
         line++;
         column = 0;
      } else {
         column++;
      }
   }

   protected void accept(final String str) {
      if(str != null) {
         column += str.length();
         int i = str.indexOf('\n', -1);
         while(i != -1) {
            line++;
            column = str.length() - i;
            i = str.indexOf('\n', i);
         }
      }
   }

   @Override
   public InternalPosition advance(final int low, final int high) {
      check(low, high);
      if(!isSuccess()) {
         return this;
      }
      return next();
   }

   @Override
   public void assertSuccess() throws PogoException {
      if(!state.isSuccess()) {
         throw state.exception();
      }
   }

   @Override
   public Memo createMemo(final String name, final Object value) {
      for(final Memo memo: memos) {
         if(name.equals(memo.name())) {
            return memo;
         }
      }
      state.newMemo(this, name, value);
      return null;
   }

   protected void fail(final int low, final int high) {
      state.addFailure(this, low, high);
   }

   @Override
   public void fail(final String reason, final Failure cause) {
      state.addFailure(this, reason, cause);
   }

   protected void fail(final String reason, final Throwable cause) {
      state.addFailure(this, reason, cause);
   }

   @Override
   public Position flush(final Position last) {
      return last;
   }

   @Override
   public InternalPosition internal() {
      return this;
   }

   @Override
   public boolean isAfter(final Position position) {
      final AbstractInternalPosition ip = (AbstractInternalPosition)((InternalPosition)position).root();
      return line > ip.line || line == ip.line && column > ip.column;
   }

   @Override
   public boolean isSuccess() {
      return state.isSuccess();
   }

   @Override
   public Adaptable match(final Position next) {
      return StandardAdaptable.UNSET;
   }

   @Override
   public Memo memo() {
      return state.memo();
   }

   @Override
   public void record(final Memo memo) {
      memos.add(memo);
   }

   @Override
   public Memo releaseMemo(final Memo newMemo) {
      final Memo memo = state.releaseMemo(newMemo);
      if(isSuccess()) {
         ((InternalPosition)memo.getStart()).record(memo);
      }
      return memo;
   }

   @Override
   public InternalPosition root() {
      return this;
   }

   @Override
   public void success() {
      state.success();
   }

   @Override
   public String toMessage() {
      return "[" + line + "," + column + "]";
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      if(state.isSuccess()) {
         builder.append('+');
      } else {
         builder.append('-');
      }
      builder.append('[').append(line).append(',').append(column).append("] \"");
      append(builder);
      return builder.append('"').toString();
   }

   @Override
   public BufferedPosition unbuffered() {
      return this;
   }
}
