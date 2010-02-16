package org.fuwjin.gravitas.gesture;

import org.fuwjin.gravitas.engine.RepeatExecution;

import com.google.inject.Inject;

@RepeatExecution(repeatEvery = 20)
public abstract class EventHandler<T> implements Runnable{
   @Inject
   private EventRouter<T> router;

   public abstract boolean handle(Integration source, T gesture) throws Exception;

   @Override
   public void run(){
      router.apply(this);
   }
}
