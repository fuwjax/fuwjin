package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.Inject;

public class CountedRepeatCommand extends Command{
   private long count;
   private long index;
   private Command command;
   @Inject
   private ExecutionEngine engine;

   @Override
   public void doRun(){
      if(index>= count){
         source().send("Finished looping");
         cancel();
      }else{
         index++;
         engine.executeNow(source(), command);
      }
   }
}
