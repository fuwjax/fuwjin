/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.chessur;

import java.util.Iterator;

/**
 * Represents a static string value.
 */
public class Literal implements Expression{
   public static final int NEW_LINE = '\n';
   public static final int TAB = '\t';
   public static final int RETURN = '\r';

   /**
    * Translates the code point to an escaped character if it's necessary
    * @param ch the code point
    * @return the escaped character
    */
   public static String dynamicEscape(final int ch){
      switch(ch){
         case '"':
            return "\\\"";
      }
      return standardEscape(ch);
   }

   /**
    * Translates the code point to an escaped character if it's necessary
    * @param ch the code point
    * @return the escaped character
    */
   public static String escape(final int ch){
      switch(ch){
         case '\'':
            return "\\'";
      }
      return standardEscape(ch);
   }

   /**
    * Converts a hex code point value to the corresponding codepoint.
    * @param hexValue the hex value
    * @return the code point
    */
   public static int parseHex(final String hexValue){
      return Integer.parseInt(hexValue, 16);
   }

   /**
    * Translates the code point to an escaped character if it's necessary
    * @param ch the code point
    * @return the escaped character
    */
   public static String standardEscape(final int ch){
      switch(ch){
         case '\n':
            return "\\n";
         case '\t':
            return "\\t";
         case '\r':
            return "\\r";
         case '\\':
            return "\\\\";
      }
      return new String(Character.toChars(ch));
   }

   private final StringBuilder builder = new StringBuilder();

   /**
    * Appends a code point to the literal.
    * @param ch the next code point
    */
   public void append(final int ch){
      builder.append(Character.toChars(ch));
   }

   /**
    * Returns the list of code points.
    * @return the code points
    */
   public Iterable<Integer> chars(){
      return new Iterable<Integer>(){
         @Override
         public Iterator<Integer> iterator(){
            return new Iterator<Integer>(){
               private int index;

               @Override
               public boolean hasNext(){
                  return index < length();
               }

               @Override
               public Integer next(){
                  final int ch = codePointAt(index);
                  index += Character.charCount(ch);
                  return ch;
               }

               @Override
               public void remove(){
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   int codePointAt(final int index){
      return builder.codePointAt(index);
   }

   int length(){
      return builder.length();
   }

   @Override
   public String toString(){
      return "'" + builder + "'";
   }

   @Override
   public State transform(final State state){
      return state.value(builder.toString());
   }
}
