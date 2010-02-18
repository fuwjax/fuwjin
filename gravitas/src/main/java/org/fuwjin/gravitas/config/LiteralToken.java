package org.fuwjin.gravitas.config;

import static org.fuwjin.util.StringUtils.word;

import java.util.Map;

public class LiteralToken implements Token{
   private String value;
   
   @Override
   public String apply(Target target, String gesture){
      String word = word(gesture);
      if(word.trim().equals(value)){
         return gesture.substring(word.length());
      }
      return null;
   }
   
   @Override
   public String value(Map<String, String> values){
      return value;
   }

   @Override
   public String toIdent(){
      return value;
   }
}
