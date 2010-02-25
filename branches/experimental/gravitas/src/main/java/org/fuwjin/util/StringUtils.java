package org.fuwjin.util;

import static java.lang.Character.isWhitespace;

public final class StringUtils{
   static{
      new StringUtils();
   }

   public static Object join(final String separator){
      return join("", separator);
   }

   public static Object join(final String initial, final String separator){
      return new Object(){
         private boolean first = true;

         @Override
         public String toString(){
            if(first){
               first = false;
               return initial;
            }
            return separator;
         }
      };
   }

   public static String word(final String string){
      return word(string, 0);
   }

   public static String word(final String string, final int start){
      int index = start;
      while(index < string.length() && !isWhitespace(string.charAt(index))){
         index++;
      }
      while(index < string.length() && isWhitespace(string.charAt(index))){
         index++;
      }
      return string.substring(start, index);
   }

   private StringUtils(){
   }
}
