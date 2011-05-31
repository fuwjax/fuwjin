package org.fuwjin.chessur.stream;

import static org.fuwjin.chessur.stream.CodePointPosition.EOF;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.fuwjin.chessur.expression.ResolveException;

/**
 * A source stream for code points.
 */
public abstract class CodePointInStream implements SourceStream {
   private static class BufferStream implements SourceStream {
      StringBuilder builder = new StringBuilder();
      private final SourceStream source;
      private final int start;

      public BufferStream(final SourceStream source) {
         this.source = source;
         start = source.index();
      }

      @Override
      public void attach(final SourceStream stream) {
         source.attach(stream);
      }

      @Override
      public SourceStream buffer() {
         return new BufferStream(this);
      }

      @Override
      public Position current() {
         return source.current();
      }

      @Override
      public SourceStream detach() {
         return new DetachedStream(index(), current(), this);
      }

      @Override
      public int index() {
         return source.index();
      }

      @Override
      public Position next(final Snapshot snapshot) throws ResolveException {
         return source.next(snapshot);
      }

      @Override
      public Position read(final Snapshot snapshot) throws ResolveException {
         final Position p = source.read(snapshot);
         if(builder.length() == source.index() - start - 1) {
            builder.append(p.valueString());
         }
         return p;
      }

      @Override
      public Position readAt(final Snapshot snapshot, final int index) throws ResolveException {
         final Position p = source.readAt(snapshot, index);
         if(builder.length() == index - start - 1) {
            builder.append(p.valueString());
         }
         return p;
      }

      @Override
      public void resume() {
         resumeAt(index());
      }

      @Override
      public void resumeAt(final int index) {
         builder.setLength(index - start);
         source.resumeAt(index);
      }

      @Override
      public String toString() {
         return builder.toString();
      }
   }

   private static class DetachedStream implements SourceStream {
      private int pos;
      private final SourceStream source;
      private Position curr;

      public DetachedStream(final int index, final Position current, final SourceStream source) {
         this.source = source;
         curr = current;
         pos = index;
      }

      @Override
      public void attach(final SourceStream stream) {
         curr = stream.current();
         pos = stream.index();
      }

      @Override
      public SourceStream buffer() {
         return new BufferStream(this);
      }

      @Override
      public Position current() {
         return curr;
      }

      @Override
      public SourceStream detach() {
         return new DetachedStream(pos, curr, source);
      }

      @Override
      public int index() {
         return pos;
      }

      @Override
      public Position next(final Snapshot snapshot) throws ResolveException {
         return source.readAt(snapshot, pos + 1);
      }

      @Override
      public Position read(final Snapshot snapshot) throws ResolveException {
         curr = source.readAt(snapshot, ++pos);
         return curr;
      }

      @Override
      public Position readAt(final Snapshot snapshot, final int index) throws ResolveException {
         return source.readAt(snapshot, index);
      }

      @Override
      public void resume() {
         resumeAt(index());
      }

      @Override
      public void resumeAt(final int index) {
         source.resumeAt(index);
      }

      @Override
      public String toString() {
         return source.toString();
      }
   }

   /**
    * The /dev/null source stream.
    */
   public static final SourceStream NONE = new CodePointInStream() {
      @Override
      protected int readChar() throws IOException {
         return EOF;
      }
   };

   /**
    * Creates a new source stream wrapping the input stream. Individual bytes
    * are rendered as code points.
    * @param input the input stream
    * @return the new source stream
    */
   public static SourceStream stream(final InputStream input) {
      return new CodePointInStream() {
         @Override
         protected int readChar() throws IOException {
            return input.read();
         }
      };
   }

   /**
    * Creates a new source stream wrapping the reader. Standard Java char's are
    * converted to code points.
    * @param reader the reader
    * @return the new source stream
    */
   public static SourceStream stream(final Reader reader) {
      return new CodePointInStream() {
         @Override
         protected int readChar() throws IOException {
            return reader.read();
         }
      };
   }

   /**
    * Creates a new source stream using the value as the input.
    * @param value the input
    * @return the new source stream
    */
   public static SourceStream streamOf(final CharSequence value) {
      return new CodePointInStream() {
         private int index = 0;

         @Override
         protected int readChar() throws IOException {
            if(index >= value.length()) {
               return EOF;
            }
            return value.charAt(index++);
         }
      };
   }

   private CodePointPosition[] queue = new CodePointPosition[10];
   {
      queue[0] = new CodePointPosition();
   }
   private int current = 0;
   private int count = 1;

   @Override
   public void attach(final SourceStream stream) {
      current = stream.index();
   }

   @Override
   public SourceStream buffer() {
      return new BufferStream(this);
   }

   @Override
   public CodePointPosition current() {
      return get(current);
   }

   @Override
   public SourceStream detach() {
      return new DetachedStream(current, current(), this);
   }

   @Override
   public int index() {
      return current;
   }

   @Override
   public Position next(final Snapshot snapshot) throws ResolveException {
      return readAt(snapshot, current + 1);
   }

   @Override
   public Position read(final Snapshot snapshot) throws ResolveException {
      final Position p = readAt(snapshot, current + 1);
      ++current;
      return p;
   }

   @Override
   public CodePointPosition readAt(final Snapshot snapshot, final int index) throws ResolveException {
      final CodePointPosition next = get(index);
      if(next.isValid()) {
         return next;
      }
      throw new ResolveException("unexpected EOF: %s", snapshot);
   }

   @Override
   public void resume() {
      // do nothing
   }

   @Override
   public void resumeAt(final int index) {
      assert current <= index;
   }

   @Override
   public String toString() {
      return current().toString();
   }

   protected CodePointPosition get(final int index) {
      while(index >= count) {
         add();
      }
      return queue[index % queue.length];
   }

   protected abstract int readChar() throws IOException;

   protected CodePointPosition readImpl() {
      final CodePointPosition tail = get(count - 1);
      try {
         final int ch = readChar();
         if(ch == EOF) {
            return tail.newPosition(EOF, 0);
         }
         if(!Character.isHighSurrogate((char)ch)) {
            return tail.newPosition(ch, 1);
         }
         final int low = readChar();
         if(low == EOF) {
            return tail.newPosition(EOF, 1);
         }
         return tail.newPosition(Character.toCodePoint((char)ch, (char)low), 2);
      } catch(final IOException e) {
         return tail.newPosition(EOF, 0);
      }
   }

   private void add() {
      if(count % queue.length == current % queue.length) {
         resize();
      }
      queue[count % queue.length] = readImpl();
      count++;
   }

   private void resize() {
      final CodePointPosition[] old = queue;
      queue = new CodePointPosition[old.length * 2];
      System.arraycopy(old, current % old.length, queue, current % queue.length, old.length - current % old.length);
      System.arraycopy(old, 0, queue, current % queue.length > old.length ? 0 : old.length, current % old.length);
   }
}
