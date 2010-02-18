package org.fuwjin.gravitas.config;

import static org.fuwjin.util.StringUtils.word;

public class LiteralToken implements Token{
   private String value;
   
   @Override
   public int apply(Target target, String elements, int index){
      String word = word(elements, index);
      if(word.trim().equals(value)){
         return index+word.length();
      }
      return NOT_APPLIED;
   }

   @Override
   public String toIdent(){
      return value;
   }
}
