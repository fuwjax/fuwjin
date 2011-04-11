package org.fuwjin.chessur;

import java.util.Arrays;

/**
 * Simple class for managing indention during serialization.
 */
public class Indent {
   private static char[] spaces = new char[100];
   static {
      Arrays.fill(spaces, ' ');
      spaces[0] = '\n';
   }
   private int width = 3;

   /**
    * Decreases the indention.
    */
   public void decrease() {
      width -= 2;
   }

   /**
    * Increases the indention.
    */
   public void increase() {
      width += 2;
   }

   @Override
   public String toString() {
      return new String(spaces, 0, width);
   }
}
