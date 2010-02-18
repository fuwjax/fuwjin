package org.fuwjin.gravitas.config;

import org.fuwjin.gravitas.engine.Command;

public class LiteralToken implements Token{
   private String value;
   
   @Override
   public int apply(Command runner, String[] elements, int index){
      return elements[index].equals(value) ? index : NOT_APPLIED;
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
