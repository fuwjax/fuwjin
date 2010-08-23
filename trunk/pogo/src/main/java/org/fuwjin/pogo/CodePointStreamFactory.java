package org.fuwjin.pogo;

import static java.lang.Character.isHighSurrogate;
import static java.lang.Character.toCodePoint;
import static java.lang.Thread.currentThread;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

public class CodePointStreamFactory {
   public static abstract class CharStream implements CodePointStream {
      @Override
      public int next() {
         try {
            final int c1 = readChar();
            if(c1 != EOF && isHighSurrogate((char)c1)) {
               final int c2 = readChar();
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

      protected abstract int readChar() throws IOException;
   }

   private static InputStream open(final String file) throws FileNotFoundException {
      InputStream s = currentThread().getContextClassLoader().getResourceAsStream(file);
      if(s == null) {
         s = ClassLoader.getSystemResourceAsStream(file);
      }
      if(s == null) {
         s = new FileInputStream(file);
      }
      return s;
   }

   public static CodePointStream stream(final InputStream stream) {
      return new CharStream() {
         @Override
         protected int readChar() throws IOException {
            return stream.read();
         }
      };
   }

   public static CodePointStream stream(final Reader reader) {
      return new CharStream() {
         @Override
         protected int readChar() throws IOException {
            return reader.read();
         }
      };
   }

   /**
    * Opens a file from the context classloader.
    * @param file the file to open
    * @return the reader for the file
    * @throws FileNotFoundException
    */
   public static CodePointStream stream(final String file) throws FileNotFoundException {
      return stream(open(file));
   }

   /**
    * Opens a file from the context classloader.
    * @param file the file to open
    * @param encoding the character encoding for the file
    * @return the reader for the file
    * @throws UnsupportedEncodingException
    * @throws FileNotFoundException
    */
   public static CodePointStream stream(final String file, final String encoding) throws UnsupportedEncodingException,
         FileNotFoundException {
      return stream(new InputStreamReader(open(file), encoding));
   }

   public static CodePointStream streamOf(final String string) {
      return stream(new StringReader(string));
   }
}
