package org.fuwjin.sample;

public class PreAdvice{
   public static void builderToString(StringBuilder builder){
      builder.setLength(0);
      builder.append("Hi Mom");
   }
}
