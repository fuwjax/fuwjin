package org.fuwjin.gravitas.gesture.command;

import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class ClearEventsCommand implements Runnable{
   @Inject
   private EventRouter<?> router;
   @Inject
   private Integration source;

   @Override
   public void run(){
      final int removed = router.clear();
      if(removed == 0){
         source.send("The queue is empty");
      }else if(removed == 1){
         source.send("Removed 1 event");
      }else{
         source.send(String.format("Removed %d events", removed));
      }
   }
}
