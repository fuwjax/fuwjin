package org.fuwjin.gravitas.gesture;

import org.fuwjin.gravitas.engine.RepeatExecution;

import com.google.inject.Inject;

@RepeatExecution(repeatEvery = 20)
public abstract class EventHandler implements Runnable{
   @Inject
   private EventRouter router;

   public abstract boolean handle(Context source, Object gesture) throws Exception;

   @Override
   public void run(){
      router.apply(this);
   }
}
