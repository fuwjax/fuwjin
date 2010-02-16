package org.fuwjin.gravitas.config;

public class LiteralToken implements Token{
   private String value;

   @Override
   public boolean apply(final Runnable runner, final String arg){
      return value.equals(arg);
   }

   @Override
   public void resolve(final Class<?> type){
      // ignore
   }

   @Override
   public String toIdent(){
      return value;
   }
}
