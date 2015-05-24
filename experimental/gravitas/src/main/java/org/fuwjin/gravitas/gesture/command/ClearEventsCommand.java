package org.fuwjin.gravitas.gesture.command;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.gesture.EventRouter;

import com.google.inject.Inject;

public class ClearEventsCommand extends Command{
   @Inject
   private EventRouter router;

   @Override
   public void doRun(){
      final int removed = router.clear();
      if(removed == 0){
         source().send("The queue is empty");
      }else if(removed == 1){
         source().send("Removed 1 event");
      }else{
         source().send(String.format("Removed %d events", removed));
      }
   }
}
