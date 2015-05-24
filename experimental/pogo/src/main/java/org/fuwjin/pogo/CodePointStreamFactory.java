/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
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

/**
 * Creates standard CodePointStreams. Technically, not all these stream code
 * points, a byte stream could also be a code point stream.
 */
public class CodePointStreamFactory {
   /**
    * Opens a new file for streaming. The file is searched for first in the
    * classpath, then in the local file system.
    * @param file the file to open.
    * @return the new input stream.
    * @throws FileNotFoundException if the file cannot be found on the classpath
    *         or file system.
    */
   public static InputStream open(final String file) throws FileNotFoundException {
      InputStream s = currentThread().getContextClassLoader().getResourceAsStream(file);
      if(s == null) {
         s = ClassLoader.getSystemResourceAsStream(file);
      }
      if(s == null) {
         s = new FileInputStream(file);
      }
      return s;
   }

   /**
    * Wraps a reader as a code point stream.
    * @param reader the reader
    * @return the new stream
    */
   public static CodePointStream stream(final Reader reader) {
      return new CodePointStream() {
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

         protected int readChar() throws IOException {
            return reader.read();
         }
      };
   }

   /**
    * Opens a file as a code point stream.
    * @param file the file to open
    * @param encoding the character encoding for the file
    * @return the new stream
    * @throws UnsupportedEncodingException if the encoding does not exist
    * @throws FileNotFoundException if the file does not exist
    */
   public static CodePointStream stream(final String file, final String encoding) throws UnsupportedEncodingException,
         FileNotFoundException {
      return stream(new InputStreamReader(open(file), encoding));
   }

   /**
    * Wraps a byte stream as a code point stream.
    * @param stream the byte stream
    * @return the new stream
    */
   public static CodePointStream streamBytes(final InputStream stream) {
      return new CodePointStream() {
         @Override
         public int next() {
            try {
               return stream.read();
            } catch(final IOException e) {
               return EOF;
            }
         }
      };
   }

   /**
    * Opens a file as a byte stream.
    * @param file the file to open
    * @return the new stream
    * @throws FileNotFoundException if the file does not exist
    */
   public static CodePointStream streamBytes(final String file) throws FileNotFoundException {
      return streamBytes(open(file));
   }

   /**
    * Wraps a string as a code point stream.
    * @param string the string to wrap
    * @return the new stream
    */
   public static CodePointStream streamOf(final String string) {
      return stream(new StringReader(string));
   }
}
