package org.fuwjin.io;

import static java.lang.Character.isHighSurrogate;
import static java.lang.Character.toCodePoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public abstract class AbstractCodePointStream implements CodePointStream {
   public static CodePointStream stream(final InputStream stream) {
      return new AbstractCodePointStream() {
         @Override
         protected int read() throws IOException {
            return stream.read();
         }
      };
   }

   public static CodePointStream stream(final Reader reader) {
      return new AbstractCodePointStream() {
         @Override
         protected int read() throws IOException {
            return reader.read();
         }
      };
   }

   @Override
   public int next() {
      try {
         final int c1 = read();
         if(c1 != EOF && isHighSurrogate((char)c1)) {
            final int c2 = read();
            if(c2 == EOF) {
               return EOF;
            }
            return toCodePoint((char)c1, (char)c2);
         }
         return c1;
      } catch(final IOException e) {
         return EOF;
      }
   }

   protected abstract int read() throws IOException;
}
