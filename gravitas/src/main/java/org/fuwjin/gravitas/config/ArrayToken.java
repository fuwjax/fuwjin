package org.fuwjin.gravitas.config;

import java.util.Map;


public class ArrayToken implements Token{
   private String name;
   
   @Override
   public String apply(Target target, String gesture) {
      if(target.set(name, gesture)){
         return "";
      }
      return null;
   }
   
   @Override
   public String value(Map<String,String> values) {
      return null;
   };

   @Override
   public String toIdent(){
      return "$["+name+"]";
   }
}
