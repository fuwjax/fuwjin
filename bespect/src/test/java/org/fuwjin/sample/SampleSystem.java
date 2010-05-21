package org.fuwjin.sample;

import static java.lang.System.nanoTime;

public class SampleSystem{
   public static long postTest(){
      return nanoTime();
   }
   
   public static String preTest(StringBuilder builder){
      return builder.toString();
   }
}
