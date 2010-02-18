package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionContextHelper;

import com.google.inject.Inject;

public class CancelCommand extends Command{
   private int jobId;
   @Inject
   private ExecutionContextHelper helper;

   @Override
   public void doRun(){
      final Execution execution = helper.execution(source(), jobId);
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
