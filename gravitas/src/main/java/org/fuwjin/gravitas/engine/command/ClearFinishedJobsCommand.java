package org.fuwjin.gravitas.engine.command;

import java.util.Iterator;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionContextHelper;

import com.google.inject.Inject;

public class ClearFinishedJobsCommand extends Command{
   @Inject
   private ExecutionContextHelper helper;
   
   @Override
   public void doRun(){
      int removed = 0;
      final Iterator<Execution> iter = helper.executions(source()).iterator();
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