package org.fuwjin.gravitas.gesture.handler;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.gesture.EventHandler;
import org.fuwjin.gravitas.gesture.EventRouter;

import com.google.inject.Inject;

public abstract class AbstractEventHandler extends Command implements EventHandler{
   @Inject
   private EventRouter router;
   private int lastId;

   @Override
   public void doRun(){
      lastId = router.apply(this, lastId);
   }
}
