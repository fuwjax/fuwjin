package org.fuwjin.grin.env;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class AcceptStream extends AbstractIoInfo<int[]> {
   private final Reader reader;

   public AcceptStream(final Reader reader) {
      this.reader = reader;
   }

   public AcceptStream(final Reader reader, final int initialFactor, final int maxFactor) {
      super(initialFactor, maxFactor);
      this.reader = reader;
   }

   public int next() throws IOException {
      if(isEmpty()) {
         return readImpl();
      }
      return codePointAt(nextIndex());
   }

   public void read() throws IOException {
      advance(next());
   }

   protected int codePointAt(final int index) {
      return array()[indexOf(index)];
   }

   @Override
   protected int[] newArray(final int size) {
      return new int[size];
   }

   protected char readChar() throws IOException {
      final int ch = reader.read();
      if(ch == -1) {
         throw new EOFException();
      }
      return (char)ch;
   }

   protected int readImpl() throws IOException {
      final int p = nextIndex();
      final char hi = readChar();
      ensureCapacity();
      if(Character.isHighSurrogate(hi)) {
         final char lo = readChar();
         ensureCapacity();
         final int ch = Character.toCodePoint(hi, lo);
         array()[indexOf(p + 1)] = -1;
         array()[indexOf(p)] = ch;
         return ch;
      }
      array()[indexOf(p)] = hi;
      return hi;
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
