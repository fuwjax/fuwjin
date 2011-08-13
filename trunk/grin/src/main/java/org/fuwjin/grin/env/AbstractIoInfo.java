package org.fuwjin.grin.env;

import java.io.IOException;
import org.fuwjin.util.BusinessException;

public abstract class AbstractIoInfo<A> {
   /**
    * The index of the value after the "current" value.
    */
   private int nextIndex;
   /**
    * The line number of the "current" value.
    */
   private int line = 1;
   /**
    * The column number of the "current" value.
    */
   private int column;
   /**
    * The index of the next value to read from the stream.
    */
   private int streamIndex;
   /**
    * The value array.
    */
   private A values;
   /**
    * The lock array.
    */
   private int[] lock;
   /**
    * The bit mask for determining array index from virtual index.
    */
   private int mask;
   /**
    * The size of the values and lock arrays.
    */
   private int size;
   private int maxSize;

   public AbstractIoInfo() {
      this(10, 30);
   }

   public AbstractIoInfo(final int startFactor, final int maxFactor) {
      size = 1 << startFactor;
      maxSize = 1 << maxFactor;
      this.values = newArray(size);
      lock = new int[size];
      mask = size - 1;
   }

   public int column() {
      return column;
   }

   public int line() {
      return line;
   }

   public int mark() {
      lock[nextIndex & mask]++;
      return nextIndex;
   }

   public void release(final int mark) throws IOException {
      lock[mark & mask]--;
   }

   public void seek(final int mark, final int line, final int column) {
      nextIndex = mark;
      this.line = line;
      this.column = column;
   }

   public Object summary() {
      return BusinessException.concatObject("[", line, ",", column < 0 ? -column : column, "] ", around());
   }

   protected void advance(final int currentValue) {
      nextIndex += Character.charCount(currentValue);
      if(currentValue == '\n') {
         column = -column - 1;
      } else if(column < 0) {
         line++;
         column = 1;
      } else {
         column++;
      }
   }

   protected void advance(final String value) {
      nextIndex++;
      int index = value.lastIndexOf('\n');
      if(index >= 0) {
         column = value.length() - index - 1;
         while(index >= 0) {
            line++;
            index = value.lastIndexOf('\n', index - 1);
         }
      } else {
         column += value.length();
      }
   }

   protected String aroundString(final int mark, final int start, final int length) {
      if(mark < streamIndex - size) {
         return "???";
      }
      if(mark == streamIndex && mark == 0) {
         return "SOF";
      }
      final int low = Math.max(0, Math.max(streamIndex - size, start));
      final int high = Math.min(streamIndex, start + length);
      final StringBuilder out = new StringBuilder("\"");
      for(int i = low; i < high; i++) {
         if(i == mark) {
            out.append("\u2380");
         }
         final String s = valueAt(i);
         if(s != null) {
            out.append(s.replace("\n", "\\n"));
         }
      }
      if(high == mark) {
         out.append("\u2380");
      }
      final String result = out.append("\"").toString();
      if("\"\u2380\"".equals(result)) {
         return "SOF";
      }
      return result;
   }

   protected A array() {
      return values;
   }

   protected void ensureCapacity() {
      if(nextIndex == streamIndex) {
         streamIndex++;
         if(lock[streamIndex & mask] > 0) {
            if(size >= maxSize) {
               throw new IllegalStateException("Cannot grow past " + maxSize);
            }
            final int newSize = size << 1;
            if((streamIndex & size) > 0) {
               values = resize(values, newArray(newSize), size, 0);
               lock = resize(lock, new int[newSize], size, 0);
            } else {
               values = resize(values, newArray(newSize), 0, size);
               lock = resize(lock, new int[newSize], 0, size);
            }
            size = newSize;
            mask = size - 1;
         }
      }
   }

   protected int indexOf(final int index) {
      if(index < streamIndex - size) {
         throw new IndexOutOfBoundsException();
      }
      return index & mask;
   }

   protected boolean isEmpty() {
      return nextIndex == streamIndex;
   }

   protected boolean isMarked(final int index) {
      return lock[index & mask] > 0;
   }

   protected abstract A newArray(int size);

   protected int nextIndex() {
      return nextIndex;
   }

   protected abstract String valueAt(int index);

   private Object around() {
      final int p = nextIndex;
      return new Object() {
         @Override
         public String toString() {
            return aroundString(p, p - 5, 10);
         }
      };
   }

   private <T>T resize(final T src, final T dest, final int off1, final int off2) {
      System.arraycopy(src, 0, dest, off1, streamIndex & mask);
      System.arraycopy(src, streamIndex & mask, dest, off2 + (streamIndex & mask), size - (streamIndex & mask));
      return dest;
   }
}
