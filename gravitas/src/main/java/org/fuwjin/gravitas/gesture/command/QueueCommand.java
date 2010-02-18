package org.fuwjin.gravitas.gesture.command;

import static org.fuwjin.util.StringUtils.join;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.gesture.Event;
import org.fuwjin.gravitas.gesture.EventRouter;

import com.google.inject.Inject;

public class QueueCommand extends Command{
   @Inject
   private EventRouter router;

   @Override
   public void doRun(){
      final StringBuilder builder = new StringBuilder();
      final Object separator = join("\n");
      for(final Event event: router.queue()){
         builder.append(separator).append(event.id()).append(") [").append(event.source().name()).append("] ").append(
               event.gesture());
      }
      if(builder.length() == 0){
         builder.append("The queue is empty");
      }
      source().send(builder);
   }
}
