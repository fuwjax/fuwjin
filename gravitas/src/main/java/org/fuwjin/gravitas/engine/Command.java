package org.fuwjin.gravitas.engine;

import org.fuwjin.gravitas.gesture.Context;

public abstract class Command implements Runnable{
   private Object gesture;
   private Context source;

   @Override
   public final void run(){
      try{
         doRun();
      }catch(final Exception e){
         System.err.println("ERROR: Could not handle " + gesture);
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   public void setGesture(final Object gesture){
      this.gesture = gesture;
   }

   public void setSource(final Context source){
      this.source = source;
   }

   @Override
   public String toString(){
      return gesture.toString();
   }

   protected abstract void doRun() throws Exception;

   protected Object gesture(){
      return gesture;
   }

   protected Context source(){
      return source;
   }
}
