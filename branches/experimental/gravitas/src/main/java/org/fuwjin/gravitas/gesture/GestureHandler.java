package org.fuwjin.gravitas.gesture;

import org.fuwjin.gravitas.engine.RepeatExecution;

import com.google.inject.Inject;

@RepeatExecution(repeatEvery=20)
public abstract class GestureHandler implements Runnable{
   @Inject
   private GestureRouter router;
   
   @Override
   public void run(){
      router.dispatch(this);
   }
   
   public abstract boolean handle(Integration source, Object gesture);
}
