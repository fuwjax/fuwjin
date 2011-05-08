package org.fuwjin.util;

public class UnicodeUtils {
   public static String toString(final int codePoint) {
      return new String(Character.toChars(codePoint));
   }
}
