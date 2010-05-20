package org.fuwjin.bespect;

import java.util.regex.Pattern;

public class BespectConfig{
   private String advice;
   private String prefix;
   private Pattern target;

   public BespectConfig(String line){
      String[] split = line.split(" ");
      advice = split[0];
      prefix = split[1];
      target = Pattern.compile(split[2]);
   }

   public String getMethodPrefix(){
      return prefix;
   }

   public String getAdvisor(){
      return advice;
   }

   public boolean isAdvised(String className){
      return target.matcher(className).matches();
   }
}
