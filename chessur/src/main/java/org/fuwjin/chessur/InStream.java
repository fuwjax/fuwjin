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
      private final int line;
      private final int col;
      private int cp;
      private Position next;

      /**
       * Creates a new position.
       * @param index the index of the position
       * @param line the previous line
       * @param col the previous column
       */
      public Position(final int index, final int line, final int col) {
         this.line = line;
         this.col = col;
         cp = EOF;
      }

      public char[] chars() {
         return Character.toChars(codePoint());
      }

      /**
       * Returns the code point at this position.
       * @return the code point
       */
      public int codePoint() {
         if(cp == EOF) {
            cp = read();
         }
         return cp;
      }

      /**
       * Returns the next position.
       * @return the next position
       */
      public Position next() {
         if(next == null) {
            final int ch = codePoint();
            if(ch == EOF) {
               next = this;
            } else if(ch == '\n') {
               next = new Position(index(), line + 1, 1);
            } else {
               next = new Position(index(), line, col + 1);
            }
         }
         return next;
      }

      public String str() {
         return new String(chars());
      }

      /**
       * Returns a substring from the mark to this position.
       * @param mark the start position
       * @return the substring
       */
      public String substring(final Position mark) {
         final StringBuilder builder = new StringBuilder();
         for(Position p = mark; p != this; p = p.next()) {
            if(p.codePoint() == EOF) {
               throw new IllegalArgumentException("mark does not precede this position");
            }
            builder.append(p.chars());
         }
         return builder.toString();
      }

      @Override
      public String toString() {
         if(codePoint() == EOF) {
            return "[" + line + "," + col + "] EOF";
         }
         return "[" + line + "," + col + "] '" + str() + "'";
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
      protected int index() {
         return 0;
      }

      @Override
      protected int readChar() {
         return EOF;
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
         protected int readChar() throws IOException {
            return stream.read();
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
         protected int readChar() throws IOException {
            return reader.read();
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
         @Override
         protected int readChar() {
            if(index() >= input.length()) {
               return EOF;
            }
            return input.charAt(index());
         }
      };
   }

   protected final StringBuilder buffer = new StringBuilder();
   protected final Position start = new Position(0, 1, 1);
   private int index;

   /**
    * Returns the start position.
    * @return the start position
    */
   public Position start() {
      return start;
   }

   protected int index() {
      return index;
   }

   protected int read() {
      try {
         int ch = readChar();
         index++;
         if(ch != EOF && Character.isHighSurrogate((char)ch)) {
            final int low = readChar();
            index++;
            if(low == EOF) {
               ch = EOF;
            } else {
               ch = Character.toCodePoint((char)ch, (char)low);
            }
         }
         return ch;
      } catch(final IOException e) {
         return EOF;
      }
   }

   protected abstract int readChar() throws IOException;
}
