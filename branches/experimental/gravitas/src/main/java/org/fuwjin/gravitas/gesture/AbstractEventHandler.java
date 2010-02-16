package org.fuwjin.gravitas.gesture;

import org.fuwjin.gravitas.engine.RepeatExecution;

import com.google.inject.Inject;

@RepeatExecution(repeatEvery = 20)
public abstract class AbstractEventHandler implements Runnable, EventHandler{
   @Inject
   private EventRouter router;
   private int lastId;

   @Override
   public void run(){
      lastId = router.apply(this, lastId);
   }
}
