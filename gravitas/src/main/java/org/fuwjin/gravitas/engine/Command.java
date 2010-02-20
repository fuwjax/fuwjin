package org.fuwjin.gravitas.engine;

import org.fuwjin.gravitas.gesture.Context;

import com.google.inject.Injector;

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
         System.err.println("ERROR: Could not handle "+gesture);
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   protected abstract void doRun() throws Exception;

   public void setSource(Context source){
      this.source = source;
   }

   public void setGesture(Object gesture){
      this.gesture = gesture;
   }

   public void inject(Injector injector){
      injector.injectMembers(this);
   }
}
