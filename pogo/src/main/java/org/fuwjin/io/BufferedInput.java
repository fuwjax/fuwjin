/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Maps a Reader to a CharSequence.
 */
public abstract class BufferedInput implements CharSequence {
   private static final int EOF = -1;

   /**
    * Buffers the input as a {@link CharSequence}.
    * @param input the input stream
    * @return the buffered char sequence.
    */
   public static CharSequence buffer(final InputStream input) {
      return new BufferedInput() {
         @Override
         protected int readImpl() throws IOException {
            return input.read();
         }
      };
   }

   /**
    * Buffers the input as a {@link CharSequence}.
    * @param input the reader
    * @return the buffered char sequence.
    */
   public static CharSequence buffer(final Reader input) {
      return new BufferedInput() {
         @Override
         protected int readImpl() throws IOException {
            return input.read();
         }
      };
   }

   private final StringBuilder buffer = new StringBuilder();

   @Override
   public char charAt(final int index) {
      while(index >= buffer.length()) {
         final int ch = read();
         if(ch == EOF) {
            throw new IndexOutOfBoundsException();
         }
         buffer.append((char)ch);
      }
      return buffer.charAt(index);
   }

   @Override
   public int length() {
      return buffer.length();
   }

   @Override
   public CharSequence subSequence(final int start, final int end) {
      try {
         return buffer.subSequence(start, end);
      } catch(final RuntimeException e) {
         throw e;
      }
   }

   private int read() {
      try {
         return readImpl();
      } catch(final IOException e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * Read a code point from the input.
    * @return the code point
    * @throws IOException if there is an IOException on the underlying input
    */
   protected abstract int readImpl() throws IOException;
}
