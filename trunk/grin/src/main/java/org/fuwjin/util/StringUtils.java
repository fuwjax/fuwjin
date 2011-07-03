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

   /**
    * Joins the string values of a set of items.
    * @param items the set of items to join
    * @return the joined string
    */
   public static String join(final Iterable<?> items) {
      final StringBuilder builder = new StringBuilder();
      for(final Object o: items) {
         builder.append(o);
      }
      return builder.toString();
   }

   /**
    * Joins the string values of a set of items separated by a delimiter.
    * @param delim the delimiter
    * @param items the set of items to join
    * @return the joined string
    */
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
}
