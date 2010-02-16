package org.fuwjin.gravitas.gesture.command;

import static org.fuwjin.util.StringUtils.join;

import org.fuwjin.gravitas.gesture.Event;
import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class QueueCommand implements Runnable{
   @Inject
   private EventRouter router;
   @Inject
   private Integration source;

   @Override
   public void run(){
      final StringBuilder builder = new StringBuilder();
      int index = 0;
      final Object separator = join("\n");
      for(final Event event: router.queue()){
         builder.append(separator).append(++index).append(") [").append(event.source().name()).append("] ").append(
               event.gesture());
      }
      if(index == 0){
         builder.append("The queue is empty");
      }
      source.send(builder);
   }
}
