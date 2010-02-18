package org.fuwjin.gravitas.config;

import static org.fuwjin.util.StringUtils.word;

public class VariableToken implements Token{
   private String name;
   
   @Override
   public int apply(Target target, String elements, int index){
      String word = word(elements, index);
      if(target.set(name, word.trim())){
         return index+word.length();
      }
      return NOT_APPLIED;
   }

   @Override
   public String toIdent(){
      return '$' + name;
   }
}
