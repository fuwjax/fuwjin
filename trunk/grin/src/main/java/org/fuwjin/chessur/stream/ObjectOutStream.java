package org.fuwjin.chessur.stream;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.fuwjin.chessur.expression.AbortedException;

/**
 * The default output stream implementation.
 */
public abstract class ObjectOutStream implements SinkStream {
   private class DetachedStream implements SinkStream {
      private int index;
      private StringPosition pos;
      private final ObjectOutStream log;

      public DetachedStream(final int i, final StringPosition current, final ObjectOutStream log) {
         index = i;
         pos = current;
         this.log = log;
      }

      @Override
      public void append(final Object value) {
         pos = add(index, pos, value);
         index++;
      }

      @Override
      public void attach(final SinkStream stream) {
         index = ((DetachedStream)stream).index();
      }

      @Override
      public Position current() {
         return pos;
      }

      @Override
      public SinkStream detach() {
         return new DetachedStream(index, pos, log);
      }

      @Override
      public void log(final Object value) {
         log.log(value);
      }

      protected int index() {
         return index;
      }
   }

   /**
    * The /dev/null output stream.
    */
   public static final SinkStream NONE = new ObjectOutStream() {
      @Override
      public void log(final Object result) {
         // do nothing
      }

      @Override
      protected void appendImpl(final String value) throws IOException {
         // do nothing
      }
   };
   /**
    * The standard output stream.
    */
   public static final SinkStream STDOUT = new ObjectOutStream() {
      @Override
      public void log(final Object value) {
         System.err.print(value);
      }

      @Override
      protected void appendImpl(final String value) throws IOException {
         System.out.print(value);
      }
   };

   /**
    * Returns the standard internal output stream. The toString() of this stream
    * will return the serialized stream.
    * @return the standard internal output stream
    */
   public static SinkStream stream() {
      return new ObjectOutStream() {
         private final StringBuilder builder = new StringBuilder();

         @Override
         public void appendImpl(final String value) {
            builder.append(value);
         }

         @Override
         public void log(final Object value) {
            // do nothing
         }

         @Override
         public String toString() {
            return builder.toString();
         }
      };
   }

   /**
    * A output stream wrapper for a PrintStream.
    * @param output the destination stream
    * @return the wrapped stream
    */
   public static SinkStream stream(final PrintStream output) {
      return new ObjectOutStream() {
         @Override
         public void appendImpl(final String value) throws IOException {
            output.print(value);
         }

         @Override
         public void log(final Object value) {
            // do nothing
         }
      };
   }

   /**
    * An output stream for a Writer.
    * @param writer the destination writer
    * @return the wrapped stream
    */
   public static SinkStream stream(final Writer writer) {
      return new ObjectOutStream() {
         @Override
         public void appendImpl(final String value) throws IOException {
            writer.append(value);
         }

         @Override
         public void log(final Object value) {
            // do nothing
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
         for(int i = 0; i < ((DetachedStream)stream).index(); i++) {
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
      return new DetachedStream(0, current, this);
   }

   protected StringPosition add(final int index, final StringPosition pos, final Object value) {
      final StringPosition p = pos.newPosition(value);
      if(index == buffer.size()) {
         buffer.add(p);
      } else {
         buffer.set(index, p);
      }
      return p;
   }

   protected abstract void appendImpl(String value) throws IOException;
}
