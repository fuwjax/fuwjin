package org.fuwjin.gravitas.engine;

import org.fuwjin.gravitas.gesture.Context;

public abstract class Command implements Runnable{
   private Object gesture;
   private Context source;
   
   protected Context source(){
      return source;
   }
   
   protected Object gesture(){
      return gesture;
   }
   
   @Override
   public String toString(){
      return gesture.toString();
   }

   @Override
   public final void run(){
      try{
         doRun();
      }catch(Exception e){
         throw new RuntimeException(e);
      }
   }

   protected abstract void doRun();

   public void setSource(Context source){
      this.source = source;
   }
}
