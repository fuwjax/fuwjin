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

public class StreamUtils {
   public static InputStream inputStream(final String path) throws FileNotFoundException {
      InputStream stream = ClassLoader.getSystemResourceAsStream(path);
      if(stream == null) {
         stream = new FileInputStream(path);
      }
      return stream;
   }

   public static OutputStream outputStream(final String path) throws FileNotFoundException {
      return new FileOutputStream(path);
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

   public static Reader reader(final InputStream in, final String encoding) throws UnsupportedEncodingException {
      return new InputStreamReader(in, encoding);
   }

   public static Reader reader(final String path, final String encoding) throws FileNotFoundException,
         UnsupportedEncodingException {
      return reader(inputStream(path), encoding);
   }

   public static Writer writer(final OutputStream out, final String encoding) throws UnsupportedEncodingException {
      return new OutputStreamWriter(out, encoding);
   }

   public static Writer writer(final String path, final String encoding) throws UnsupportedEncodingException,
         FileNotFoundException {
      return writer(outputStream(path), encoding);
   }
}
