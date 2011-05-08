package org.fuwjin.chessur.stream;

import static org.fuwjin.chessur.stream.CodePointPosition.EOF;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import org.fuwjin.chessur.expression.ResolveException;

public abstract class CodePointInStream implements SourceStream {
   private class DetachedStream implements SourceStream {
      private final int start;
      private int pos;

      public DetachedStream(final int start, final int pos) {
         this.start = start;
         this.pos = pos;
      }

      @Override
      public void attach(final SourceStream stream) {
         pos = stream.current().index();
      }

      @Override
      public Iterable<? extends Position> buffer(final Snapshot snapshot) throws ResolveException {
         return positions(snapshot, start, pos);
      }

      @Override
      public Position current() {
         return get(pos);
      }

      @Override
      public SourceStream detach() {
         return new DetachedStream(start, pos);
      }

      @Override
      public SourceStream mark() {
         return new MarkedStream(pos, this);
      }

      @Override
      public Position next(final Snapshot snapshot) throws ResolveException {
         return getImpl(snapshot, pos + 1);
      }

      @Override
      public Position read(final Snapshot snapshot) throws ResolveException {
         return getImpl(snapshot, ++pos);
      }
   }

   private class MarkedStream implements SourceStream {
      private final int mark;
      private final SourceStream source;

      public MarkedStream(final int mark, final SourceStream stream) {
         this.mark = mark;
         source = stream;
      }

      @Override
      public void attach(final SourceStream stream) {
         source.attach(stream);
      }

      @Override
      public Iterable<? extends Position> buffer(final Snapshot snapshot) throws ResolveException {
         return positions(snapshot, mark, source.current().index());
      }

      @Override
      public Position current() {
         return source.current();
      }

      @Override
      public SourceStream detach() {
         return new DetachedStream(mark, source.current().index());
      }

      @Override
      public SourceStream mark() {
         return source.mark();
      }

      @Override
      public Position next(final Snapshot snapshot) throws ResolveException {
         return source.next(snapshot);
      }

      @Override
      public Position read(final Snapshot snapshot) throws ResolveException {
         return source.read(snapshot);
      }
   }

   public static final SourceStream NONE = new CodePointInStream() {
      @Override
      protected int readChar() throws IOException {
         return EOF;
      }
   };
   public static final SourceStream STDIN = new CodePointInStream() {
      @Override
      protected int readChar() throws IOException {
         return System.in.read();
      }
   };

   public static SourceStream stream(final InputStream input) {
      return new CodePointInStream() {
         @Override
         protected int readChar() throws IOException {
            return input.read();
         }
      };
   }

   public static SourceStream stream(final Reader reader) {
      return new CodePointInStream() {
         @Override
         protected int readChar() throws IOException {
            return reader.read();
         }
      };
   }

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

   public static String stringOf(final Iterable<? extends Position> positions) {
      final StringBuilder builder = new StringBuilder();
      for(final Position p: positions) {
         builder.append(p.valueString());
      }
      return builder.toString();
   }

   private CodePointPosition[] queue = new CodePointPosition[10];
   {
      queue[0] = new CodePointPosition();
   }
   private int current = 0;
   private int count = 1;

   @Override
   public void attach(final SourceStream stream) {
      current = stream.current().index();
   }

   @Override
   public Iterable<CodePointPosition> buffer(final Snapshot snapshot) throws ResolveException {
      return positions(snapshot, 0, current);
   }

   @Override
   public CodePointPosition current() {
      return get(current);
   }

   @Override
   public SourceStream detach() {
      return new DetachedStream(0, current);
   }

   @Override
   public SourceStream mark() {
      return new MarkedStream(current, this);
   }

   @Override
   public Position next(final Snapshot snapshot) throws ResolveException {
      return getImpl(snapshot, current + 1);
   }

   public Iterable<CodePointPosition> positions(final Snapshot snapshot, final int start, final int end)
         throws ResolveException {
      if(end >= count || start < count - queue.length || get(start) == null) {
         throw new ResolveException(snapshot, "Could not iterate over %d to %d", start, end);
      }
      return new Iterable<CodePointPosition>() {
         @Override
         public Iterator<CodePointPosition> iterator() {
            return new Iterator<CodePointPosition>() {
               private int index = start;

               @Override
               public boolean hasNext() {
                  return index < end;
               }

               @Override
               public CodePointPosition next() {
                  return get(++index);
               }

               @Override
               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   @Override
   public Position read(final Snapshot snapshot) throws ResolveException {
      final Position p = getImpl(snapshot, current + 1);
      current++;
      return p;
   }

   protected CodePointPosition get(final int index) {
      while(index >= count) {
         add();
      }
      return queue[index % queue.length];
   }

   protected CodePointPosition getImpl(final Snapshot snapshot, final int index) throws ResolveException {
      final CodePointPosition next = get(index);
      if(next.isValid()) {
         return next;
      }
      throw new ResolveException(snapshot, "unexpected EOF");
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
