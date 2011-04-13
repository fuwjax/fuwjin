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
public abstract class OutStream{
   /**
    * The output stream position.
    */
   public class Position{
      int index;

      Position(final int index){
         this.index = index;
      }

      /**
       * Appends the value to the output stream.
       * @param value the value to append
       * @return the new output stream position
       */
      public Position append(final Object value){
         return buffer(index, value);
      }

      @Override
      public String toString(){
         return Integer.toString(index);
      }
   }

   /**
    * The dev/null output stream.
    */
   public static final OutStream NONE = new OutStream(){
      @Override
      protected void append(final Object value){
         // do nothing
      }
   };
   /**
    * The standard out output stream.
    */
   public static final OutStream STDOUT = new OutStream(){
      @Override
      protected void append(final Object value){
         System.out.print(value);
      }
   };

   public static OutStream stream(){
      return new OutStream(){
         private final StringBuilder builder = new StringBuilder();

         @Override
         protected void append(final Object value){
            builder.append(value);
         }

         @Override
         public String toString(){
            return builder.toString();
         }
      };
   }

   public static OutStream stream(final Writer writer){
      return new OutStream(){
         @Override
         protected void append(final Object value){
            try{
               writer.append(String.valueOf(value));
            }catch(final IOException e){
               throw new RuntimeException(e);
            }
         }
      };
   }

   private final StringBuilder buffer = new StringBuilder();

   protected abstract void append(Object value);

   Position buffer(final int index, final Object value){
      buffer.setLength(index);
      buffer.append(value);
      return new Position(buffer.length());
   }

   /**
    * Returns the starting position.
    * @return the starting position
    */
   public Position start(){
      return new Position(0){
         @Override
         public Position append(final Object value){
            OutStream.this.append(value);
            return this;
         }
      };
   }
}
