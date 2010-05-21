package org.fuwjin.sample;

public class PreAdvice{
   public static void preTest(StringBuilder builder){
      builder.setLength(0);
      builder.append("Hi Mom");
   }
}
