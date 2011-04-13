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

   public static Reader reader(final String path, final String encoding) throws FileNotFoundException,
         UnsupportedEncodingException {
      return new InputStreamReader(inputStream(path), encoding);
   }

   public static Writer writer(final String path, final String encoding) throws UnsupportedEncodingException,
         FileNotFoundException {
      return new OutputStreamWriter(outputStream(path), encoding);
   }
}
