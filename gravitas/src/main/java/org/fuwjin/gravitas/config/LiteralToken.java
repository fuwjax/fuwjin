package org.fuwjin.gravitas.config;

import static org.fuwjin.util.StringUtils.word;

import org.fuwjin.gravitas.engine.Command;

public class LiteralToken implements Token{
   private String value;
   
   @Override
   public int apply(Command runner, String elements, int index){
      String word = word(elements, index);
      return word.trim().equals(value) ? index+word.length() : NOT_APPLIED;
   }

   @Override
   public void resolve(final ContextConfig context, final Class<?> type){
      // ignore
   }

   @Override
   public String toIdent(){
      return value;
   }
}
