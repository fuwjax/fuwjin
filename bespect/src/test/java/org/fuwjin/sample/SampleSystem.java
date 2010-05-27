package org.fuwjin.sample;

import static java.lang.System.nanoTime;

public class SampleSystem{
   public static long getTime(){
      return nanoTime();
   }
   
   public static String builderToString(StringBuilder builder){
      return builder.toString();
   }
   
   public static int addTwoNumbers(int x, int y){
      return x + y;
   }
}
