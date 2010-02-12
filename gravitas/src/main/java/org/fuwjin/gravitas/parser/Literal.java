package org.fuwjin.gravitas.parser;

class Literal implements Atom{
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
