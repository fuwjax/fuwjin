package org.fuwjin.util;

/**
 * Utility methods supporting Unicode.
 */
public class UnicodeUtils {
   /**
    * Returns the string value of the code point.
    * @param codePoint the code point
    * @return the string value
    */
   public static String toString(final int codePoint) {
      return new String(Character.toChars(codePoint));
   }
}
