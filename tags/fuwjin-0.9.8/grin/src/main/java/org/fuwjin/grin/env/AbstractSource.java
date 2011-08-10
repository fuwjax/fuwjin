package org.fuwjin.grin.env;

import java.io.IOException;
import org.fuwjin.chessur.expression.AbortedException;
import org.fuwjin.chessur.expression.ResolveException;

public abstract class AbstractSource extends AbstractIoInfo<int[]> implements Source {
   public Match newMatch() {
      return new Match() {
         private final int start = mark();

         @Override
         public void release() throws AbortedException {
            AbstractSource.this.release(start);
         }

         @Override
         public String toString() {
            return substring(start);
         }
      };
   }

   @Override
   public int next() throws ResolveException {
      int codePoint;
      if(isEmpty()) {
         codePoint = readImpl();
      } else {
         codePoint = codePointAt(nextIndex());
      }
      if(codePoint == EOF) {
         throw new ResolveException("unexpected EOF: %s -> [1,0] SOF", summary());
      }
      return codePoint;
   }

   @Override
   public void read() throws ResolveException {
      final int codePoint = next();
      advance(codePoint);
   }

   protected int codePointAt(final int index) {
      return array()[indexOf(index)];
   }

   @Override
   protected int[] newArray(final int size) {
      return new int[size];
   }

   protected abstract int readChar() throws IOException;

   protected int readImpl() {
      int ch;
      final int p = nextIndex();
      ensureCapacity();
      try {
         ch = readChar();
         if(ch != EOF && Character.isHighSurrogate((char)ch)) {
            final char hi = (char)ch;
            ch = readChar();
            if(ch != EOF) {
               ch = Character.toCodePoint(hi, (char)ch);
               ensureCapacity();
               array()[indexOf(p + 1)] = EOF;
            }
         }
      } catch(final IOException e) {
         ch = EOF;
      }
      array()[indexOf(p)] = ch;
      return ch;
   }

   protected String substring(final int start) {
      final StringBuilder builder = new StringBuilder();
      int index = start;
      while(index < nextIndex()) {
         final int codePoint = codePointAt(index);
         builder.append(Character.toChars(codePoint));
         index += Character.charCount(codePoint);
      }
      return builder.toString();
   }

   @Override
   protected String valueAt(final int index) {
      final int ch = codePointAt(index);
      switch(ch) {
      case EOF:
         return null;
      case '\n':
         return "\\n";
      case '\t':
         return "\\t";
      case '\r':
         return "\\r";
      case '\\':
         return "\\\\";
      default:
         return new String(Character.toChars(ch));
      }
   }
}
