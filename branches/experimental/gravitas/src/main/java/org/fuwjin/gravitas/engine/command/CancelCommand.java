package org.fuwjin.gravitas.engine.command;

import static org.fuwjin.gravitas.engine.ExecutionEngine.execution;

import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.gesture.Context;

import com.google.inject.Inject;

public class CancelCommand implements Runnable{
   private int jobId;
   @Inject
   private Context source;

   @Override
   public void run(){
      final Execution execution = execution(source, jobId);
      if(execution == null){
         source.send("There is no job " + jobId);
      }else{
         if(execution.cancel()){
            source.send("Job " + jobId + " has been cancelled");
         }else{
            source.send("Could not cancel job " + jobId);
         }
      }
   }
}
