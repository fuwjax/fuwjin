package org.fuwjin.util;

import java.io.IOException;
import java.io.Reader;

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

   /**
    * Returns the completely consumed reader as a string.
    * @param reader the reader to read fully
    * @return the full output of the reader as a string
    * @throws IOException if the read fails
    */
   public static String readAll(final Reader reader) throws IOException {
      final StringBuilder builder = new StringBuilder();
      final char[] buffer = new char[100];
      int count = reader.read(buffer);
      while(count >= 0) {
         builder.append(buffer, 0, count);
         count = reader.read(buffer);
      }
      return builder.toString();
   }
}
