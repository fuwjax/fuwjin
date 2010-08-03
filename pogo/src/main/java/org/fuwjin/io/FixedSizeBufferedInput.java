/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Maps a Reader to a CharSequence.
 */
public abstract class FixedSizeBufferedInput implements CharSequence {
   private static final int EOF = -1;

   /**
    * Buffers the input as a {@link CharSequence}.
    * @param input the input stream
    * @return the buffered char sequence.
    */
   public static CharSequence buffer(final int size, final InputStream input) {
      return new FixedSizeBufferedInput(size) {
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
   public static CharSequence buffer(final int size, final Reader input) {
      return new FixedSizeBufferedInput(size) {
         @Override
         protected int readImpl() throws IOException {
            return input.read();
         }
      };
   }

   private final char[] buffer;
   private int len;

   public FixedSizeBufferedInput(final int size) {
      buffer = new char[size];
   }

   @Override
   public char charAt(final int index) {
      if(index < len - buffer.length) {
         throw new IndexOutOfBoundsException();
      }
      while(index >= len) {
         final int ch = read();
         if(ch == EOF) {
            throw new IndexOutOfBoundsException();
         }
         buffer[len++ % buffer.length] = (char)ch;
      }
      return buffer[index % buffer.length];
   }

   @Override
   public int length() {
      return len;
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

   @Override
   public CharSequence subSequence(final int start, final int end) {
      final int count = end - start;
      if(start < 0 || start < len - buffer.length || end > len || start > end) {
         throw new IndexOutOfBoundsException();
      }
      final int offset = start % buffer.length;
      final int rem = buffer.length - offset;
      if(count < rem) {
         return new String(buffer, offset, count);
      }
      final char[] buf = new char[count];
      System.arraycopy(buffer, offset, buf, 0, rem);
      System.arraycopy(buffer, 0, buf, rem, count - rem);
      return new String(buf);
   }
}
