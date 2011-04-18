/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.chessur;

import java.io.IOException;
import java.io.Writer;

/**
 * The output stream.
 */
public abstract class OutStream {
   /**
    * The output stream position.
    */
   public class Position {
      private final int index;
      private final int line;
      private final int col;
      private final int codePoint;

      /**
       * Creates a new position.
       * @param index the index of the position
       * @param line the previous line
       * @param col the previous column
       */
      public Position(final int index, final int line, final int col, final int codePoint) {
         this.index = index;
         this.line = line;
         this.col = col;
         this.codePoint = codePoint;
      }

      /**
       * Returns the next position.
       * @return the next position
       */
      public Position append(final Object value) {
         final String val = String.valueOf(value);
         OutStream.this.append(val);
         int l = line;
         int c = col + val.length();
         int i = val.indexOf('\n');
         while(i > -1) {
            l++;
            c = val.length() - i - 1;
            i = val.indexOf('\n', i + 1);
         }
         return new Position(index + val.length(), l, c, val.codePointBefore(val.length()));
      }

      @Override
      public String toString() {
         if(codePoint == -1) {
            return "[" + line + "," + col + "] SOF";
         }
         return "[" + line + "," + col + "] '" + new String(Character.toChars(codePoint)) + "'";
      }
   }

   public static int EOF = -1;
   /**
    * The dev/null output stream.
    */
   public static final OutStream NONE = new OutStream() {
      @Override
      protected void append(final Object value) {
         // do nothing
      }
   };
   /**
    * The standard out output stream.
    */
   public static final OutStream STDOUT = new OutStream() {
      @Override
      protected void append(final Object value) {
         System.out.print(value);
      }
   };

   public static OutStream stream() {
      return new OutStream() {
         private final StringBuilder builder = new StringBuilder();

         @Override
         public String toString() {
            return builder.toString();
         }

         @Override
         protected void append(final Object value) {
            builder.append(value);
         }
      };
   }

   public static OutStream stream(final Writer writer) {
      return new OutStream() {
         @Override
         protected void append(final Object value) {
            try {
               writer.append(String.valueOf(value));
            } catch(final IOException e) {
               throw new RuntimeException(e);
            }
         }
      };
   }

   private final Position start = new Position(0, 1, 0, EOF);

   /**
    * Returns the starting position.
    * @return the starting position
    */
   public Position start() {
      return start;
   }

   protected abstract void append(Object value);
}
