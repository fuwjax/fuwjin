package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.Inject;

public class CancelCommand extends Command{
   private int jobId;
   @Inject
   private ExecutionEngine engine;

   @Override
   public void doRun(){
      final Execution execution = engine.execution(source(), jobId);
      if(execution == null){
         source().send("There is no job " + jobId);
      }else{
         if(execution.cancel()){
            source().send("Job " + jobId + " has been cancelled");
         }else{
            source().send("Could not cancel job " + jobId);
         }
      }
   }
}
