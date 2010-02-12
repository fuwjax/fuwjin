package org.fuwjin.gravitas.util;

public final class StringUtils{
   private StringUtils(){}
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
}
