package org.fuwjin.grin.env;

import java.io.IOException;
import java.io.Writer;

public class PublishStream extends AbstractStream<String[]> {
   private int locks;
   private int index;
   private final Writer writer;

   public PublishStream(final Writer writer) {
      this.writer = writer;
   }

   public void append(final Object value) throws IOException {
      final String str = value == null ? "" : value.toString();
      final int p = nextIndex();
      ensureCapacity();
      array()[indexOf(p)] = str;
      if(locks == 0) {
         writer.append(str);
         index++;
      }
      advance(str);
   }

   @Override
   public int mark() {
      locks++;
      return super.mark();
   }

   @Override
   public void release(final int mark) throws IOException {
      super.release(mark);
      locks--;
      while(index < nextIndex() && !isMarked(index)) {
         final String str = valueAt(index);
         writer.append(str);
         index++;
      }
   }

   @Override
   protected String[] newArray(final int size) {
      return new String[size];
   }

   @Override
   protected String valueAt(final int index) {
      return array()[indexOf(index)];
   }
}
