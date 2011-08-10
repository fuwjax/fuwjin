package org.fuwjin.grin.env;

import java.io.IOException;
import org.fuwjin.chessur.expression.AbortedException;

public abstract class AbstractSink extends AbstractIoInfo<String[]> implements Sink {
   private int locks;
   private int index;

   @Override
   public void append(final Object value) {
      try {
         final String str = value == null ? "" : value.toString();
         final int p = nextIndex();
         ensureCapacity();
         array()[indexOf(p)] = str;
         if(locks == 0) {
            appendImpl(str);
            index++;
         }
         advance(str);
      } catch(final IOException e) {
         // do nothing?
      }
   }

   @Override
   public int mark() {
      locks++;
      return super.mark();
   }

   @Override
   public void release(final int mark) throws AbortedException {
      super.release(mark);
      locks--;
      while(index < nextIndex() && !isMarked(index)) {
         try {
            appendImpl(valueAt(index));
         } catch(final IOException e) {
            throw new AbortedException(e, "Could not publish to output stream");
         }
         index++;
      }
   }

   protected abstract void appendImpl(String value) throws IOException;

   @Override
   protected String[] newArray(final int size) {
      return new String[size];
   }

   @Override
   protected String valueAt(final int index) {
      return array()[indexOf(index)];
   }
}
