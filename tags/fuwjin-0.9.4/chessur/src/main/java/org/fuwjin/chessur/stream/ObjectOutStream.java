package org.fuwjin.chessur.stream;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.fuwjin.chessur.expression.AbortedException;

public abstract class ObjectOutStream implements SinkStream {
   private class DetachedStream implements SinkStream {
      private int index;
      private StringPosition pos;

      public DetachedStream(final int i, final StringPosition current) {
         index = i;
         this.pos = current;
      }

      @Override
      public void append(final Object value) {
         pos = pos.newPosition(value);
         if(index == buffer.size()) {
            buffer.add(pos);
         } else {
            buffer.set(index, pos);
         }
         index++;
      }

      @Override
      public void attach(final SinkStream stream) {
         index = ((DetachedStream)stream).index;
      }

      @Override
      public Position current() {
         return pos;
      }

      @Override
      public SinkStream detach() {
         return new DetachedStream(index, pos);
      }
   }

   public static final SinkStream NONE = new ObjectOutStream() {
      @Override
      protected void appendImpl(final String value) throws IOException {
         // do nothing
      }
   };
   public static final SinkStream STDOUT = new ObjectOutStream() {
      @Override
      protected void appendImpl(final String value) throws IOException {
         System.out.print(value);
      }
   };

   public static SinkStream stream() {
      return new ObjectOutStream() {
         private final StringBuilder builder = new StringBuilder();

         @Override
         public void appendImpl(final String value) {
            builder.append(value);
         }

         @Override
         public String toString() {
            return builder.toString();
         }
      };
   }

   public static SinkStream stream(final PrintStream output) {
      return new ObjectOutStream() {
         @Override
         public void appendImpl(final String value) throws IOException {
            output.print(value);
         }
      };
   }

   public static SinkStream stream(final Writer writer) {
      return new ObjectOutStream() {
         @Override
         public void appendImpl(final String value) throws IOException {
            writer.append(value);
         }
      };
   }

   private final List<StringPosition> buffer = new ArrayList<StringPosition>();
   private StringPosition current = new StringPosition();

   @Override
   public void append(final Object value) throws AbortedException {
      try {
         current = current.newPosition(value);
         appendImpl(current.valueString());
      } catch(final IOException e) {
         throw new AbortedException(e, "Could not append value to stream");
      }
   }

   @Override
   public void attach(final SinkStream stream) throws AbortedException {
      try {
         for(int i = 0; i < ((DetachedStream)stream).index; i++) {
            appendImpl(buffer.get(i).valueString());
         }
         buffer.clear();
         current = (StringPosition)stream.current();
      } catch(final IOException e) {
         throw new AbortedException(e, "Could not attach stream");
      }
   }

   @Override
   public Position current() {
      return current;
   }

   @Override
   public SinkStream detach() {
      return new DetachedStream(0, current);
   }

   protected abstract void appendImpl(String value) throws IOException;
}