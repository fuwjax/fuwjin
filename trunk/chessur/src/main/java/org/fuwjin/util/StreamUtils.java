/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * Utility methods for interacting with Java IO.
 */
public class StreamUtils {
   /**
    * Creates an input stream from the classpath, if possible, otherwise from
    * the file system.
    * @param path the path to the file
    * @return the new input stream
    * @throws FileNotFoundException if the path does not point to a file
    */
   public static InputStream inputStream(final String path) throws FileNotFoundException {
      InputStream stream = ClassLoader.getSystemResourceAsStream(path);
      if(stream == null) {
         stream = new FileInputStream(path);
      }
      return stream;
   }

   /**
    * Creates an output stream on the file system.
    * @param path the path to the output file
    * @return the new output system
    * @throws FileNotFoundException if the file cannot be created for the path
    */
   public static OutputStream outputStream(final String path) throws FileNotFoundException {
      return new FileOutputStream(path);
   }

   /**
    * Returns the completely consumed input stream as a string using the
    * platform default character set.
    * @param input the input stream to read fully
    * @return the full output of the reader as a string
    * @throws IOException if the read fails
    */
   public static String readAll(final InputStream input) throws IOException {
      final StringBuilder builder = new StringBuilder();
      final byte[] buffer = new byte[100];
      int count = input.read(buffer);
      while(count >= 0) {
         builder.append(new String(buffer, 0, count));
         count = input.read(buffer);
      }
      return builder.toString();
   }

   /**
    * Returns the completely consumed reader as a string.
    * @param reader the reader to read fully
    * @return the full output of the reader as a string
    * @throws IOException if the read fails
    */
   public static String readAll(final Reader reader) throws IOException {
      final StringBuilder builder = new StringBuilder();
      final char[] buffer = new char[100];
      int count = reader.read(buffer);
      while(count >= 0) {
         builder.append(buffer, 0, count);
         count = reader.read(buffer);
      }
      return builder.toString();
   }

   /**
    * Creates a reader wrapping the input stream using the specified character
    * encoding.
    * @param in the input stream
    * @param encoding the character encoding
    * @return the new reader
    * @throws UnsupportedEncodingException if the specified encoding is not a
    *         valid encoding on this platform
    */
   public static Reader reader(final InputStream in, final String encoding) throws UnsupportedEncodingException {
      return new InputStreamReader(in, encoding);
   }

   /**
    * Creates an reader from the classpath, if possible, otherwise from the file
    * system using the specified character encoding.
    * @param path the input stream
    * @param encoding the character encoding
    * @return the new reader
    * @throws FileNotFoundException if the path does not point to a file
    * @throws UnsupportedEncodingException if the specified encoding is not a
    *         valid encoding on this platform
    */
   public static Reader reader(final String path, final String encoding) throws FileNotFoundException,
         UnsupportedEncodingException {
      return reader(inputStream(path), encoding);
   }

   /**
    * Creates a writer wrapping the output stream using the specified character
    * encoding.
    * @param out the output stream
    * @param encoding the character encoding
    * @return the new writer
    * @throws UnsupportedEncodingException
    */
   public static Writer writer(final OutputStream out, final String encoding) throws UnsupportedEncodingException {
      return new OutputStreamWriter(out, encoding);
   }

   /**
    * Creates a writer on the file system using the specified character
    * encoding.
    * @param path the path to the file
    * @param encoding the character encoding
    * @return the new writer
    * @throws UnsupportedEncodingException
    * @throws FileNotFoundException if the file cannot be created or opened for
    *         writing
    */
   public static Writer writer(final String path, final String encoding) throws UnsupportedEncodingException,
         FileNotFoundException {
      return writer(outputStream(path), encoding);
   }
}
