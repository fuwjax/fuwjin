package org.fuwjin.gravitas.engine.command;

import java.util.Iterator;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.Inject;

public class ClearFinishedJobsCommand extends Command{
   @Inject
   private ExecutionEngine engine;

   @Override
   public void doRun(){
      int removed = 0;
      final Iterator<Execution> iter = engine.executions(source()).iterator();
      while(iter.hasNext()){
         if(iter.next().isDone()){
            iter.remove();
            removed++;
         }
      }
      if(removed == 1){
         source().send("Removed 1 job");
      }else{
         source().send(String.format("Removed %d jobs", removed));
      }
   }
}
