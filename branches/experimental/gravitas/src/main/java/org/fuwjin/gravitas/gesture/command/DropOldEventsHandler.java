package org.fuwjin.gravitas.gesture.command;

import static java.util.concurrent.TimeUnit.SECONDS;

import org.fuwjin.gravitas.engine.RepeatExecution;
import org.fuwjin.gravitas.gesture.Event;
import org.fuwjin.gravitas.gesture.EventHandler;
import org.fuwjin.gravitas.gesture.EventRouter;

import com.google.inject.Inject;

@RepeatExecution(waitBetween=10,unit=SECONDS)
public class DropOldEventsHandler implements Runnable, EventHandler{
   @Inject
   private EventRouter router;
   private int lastId;

   @Override
   public void run(){
      lastId = router.apply(this, 0);
   }

   @Override
   public boolean handle(Event event) throws Exception{
      return event.id() <= lastId;
   }
}
