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
package org.fuwjin.chessur;

import static java.lang.Character.charCount;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Manages a rewindable input stream.
 */
public abstract class InStream {
   /**
    * The input stream position.
    */
   public class Position {
      private final int index;
      private final int line;
      private final int col;

      /**
       * Creates a new position.
       * @param index the index of the position
       * @param line the previous line
       * @param col the previous column
       */
      public Position(final int index, final int line, final int col) {
         this.index = index;
         this.line = line;
         this.col = col;
      }

      /**
       * Returns the code point at this position.
       * @return the code point
       */
      public int codePoint() {
         if(index >= buffer.length()) {
            readTo(index);
            if(index >= buffer.length()) {
               return EOF;
            }
         }
         return buffer.codePointAt(index);
      }

      /**
       * Returns the next position.
       * @return the next position
       */
      public Position next() {
         final int ch = codePoint();
         if(ch == '\n') {
            return new Position(index + charCount(ch), line + 1, 1);
         }
         return new Position(index + charCount(ch), line, col + 1);
      }

      /**
       * Returns a substring from the mark to this position.
       * @param mark the start position
       * @return the substring
       */
      public String substring(final Position mark) {
         return buffer.substring(mark.index, index);
      }

      @Override
      public String toString() {
         if(index >= buffer.length()) {
            readTo(index);
            if(index >= buffer.length()) {
               return "EOF";
            }
         }
         return index + "[" + line + "," + col + "]=" + new String(Character.toChars(codePoint()));
      }
   }

   /**
    * The End of File character.
    */
   public static final int EOF = -1;
   /**
    * The standard input stream.
    */
   public static final InStream STDIN = InStream.stream(System.in);
   /**
    * The dev/null input stream.
    */
   public static final InStream NONE = new InStream() {
      @Override
      protected void readTo(final int index) {
         // do nothing
      }
   };

   /**
    * Creates a new instance.
    * @param stream the stream
    * @return the input stream
    */
   public static InStream stream(final InputStream stream) {
      return new InStream() {
         @Override
         protected void readTo(final int index) {
            final byte[] buf = new byte[Math.max(index - buffer.length(), 100)];
            try {
               if(stream.available() > 0) {
                  final int c = stream.read(buf);
                  if(c > -1) {
                     buffer.append(new String(buf, 0, c));
                  }
               }
            } catch(final IOException e) {
               // continue
            }
         }
      };
   }

   /**
    * Creates a new instance.
    * @param reader the reader to stream
    * @return the input stream
    */
   public static InStream stream(final Reader reader) {
      return new InStream() {
         @Override
         protected void readTo(final int index) {
            final char[] buf = new char[Math.max(index - buffer.length(), 100)];
            try {
               final int c = reader.read(buf);
               if(c > -1) {
                  buffer.append(buf, 0, c);
               }
            } catch(final IOException e) {
               // continue
            }
         }
      };
   }

   /**
    * Creates a new instance.
    * @param input the input
    * @return a new input stream
    */
   public static InStream streamOf(final String input) {
      return new InStream() {
         {
            buffer.append(input);
         }

         @Override
         protected void readTo(final int index) {
            // do nothing
         }
      };
   }

   protected final StringBuilder buffer = new StringBuilder();

   protected abstract void readTo(final int index);

   /**
    * Returns the start position.
    * @return the start position
    */
   public Position start() {
      return new Position(0, 1, 0);
   }
}
