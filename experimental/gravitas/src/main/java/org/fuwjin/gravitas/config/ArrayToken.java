package org.fuwjin.gravitas.config;

import java.util.Map;

public class ArrayToken implements Token{
   private String name;

   @Override
   public String apply(final Target target, final String gesture){
      if(target.set(name, gesture)){
         return "";
      }
      return null;
   }

   @Override
   public String toIdent(){
      return "$[" + name + "]";
   };

   @Override
   public String value(final Map<String, String> values){
      return null;
   }
}
