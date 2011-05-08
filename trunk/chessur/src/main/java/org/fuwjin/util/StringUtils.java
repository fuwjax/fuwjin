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

/**
 * Supporting string utilities.
 */
public class StringUtils {
   private static final String EOL = "\n\u0085\u000B\u000C\u2028\u2029";

   /**
    * Concatenates a set of strings.
    * @param strings the set of strings
    * @return the concatenated strings
    */
   public static String concatenate(final Object... strings) {
      final StringBuilder builder = new StringBuilder();
      for(final Object s: strings) {
         builder.append(s);
      }
      return builder.toString();
   }

   public static boolean isEndOfLine(final int codepoint) {
      return EOL.indexOf(codepoint) >= -1;
   }

   public static String join(final Iterable<?> items) {
      final StringBuilder builder = new StringBuilder();
      for(final Object o: items) {
         builder.append(o);
      }
      return builder.toString();
   }

   public static String join(final String delim, final Iterable<?> items) {
      final StringBuilder builder = new StringBuilder();
      boolean first = true;
      for(final Object item: items) {
         if(first) {
            first = false;
         } else {
            builder.append(delim);
         }
         builder.append(item);
      }
      return builder.toString();
   }

   public static Object pattern(final String pattern, final Object... args) {
      return new Object() {
         @Override
         public String toString() {
            return String.format(pattern, args);
         }
      };
   }
}
