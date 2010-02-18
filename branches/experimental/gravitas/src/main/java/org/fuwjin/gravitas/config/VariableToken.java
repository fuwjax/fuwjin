package org.fuwjin.gravitas.config;

import static org.fuwjin.util.StringUtils.word;

import java.util.Map;

public class VariableToken implements Token{
   private String name;
   
   @Override
   public String apply(Target target, String gesture){
      String word = word(gesture);
      if(target.set(name, word.trim())){
         return gesture.substring(word.length());
      }
      return null;
   }
   
   @Override
   public String value(Map<String, String> values){
      return values.get(name);
   }

   @Override
   public String toIdent(){
      return '$' + name;
   }
}
