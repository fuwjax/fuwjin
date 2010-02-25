package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.engine.Execution.Status;

import com.google.inject.Inject;

public class SingleStatusCommand extends Command{
   private int jobId;
   @Inject
   private ExecutionEngine engine;

   @Override
   public void doRun(){
      final StringBuilder builder = new StringBuilder();
      Execution execution = null;
      if(jobId == 0){
         execution = engine.previousExecution(source());
      }else{
         execution = engine.execution(source(),jobId);
      }
      if(execution == null){
         builder.append("There is no job " + jobId);
      }else{
         builder.append(execution.id()).append(") [").append(execution.status()).append("] ").append(execution.desc());
         if(execution.status() == Status.Failed){
            builder.append('\n').append(execution.failure());
         }
      }
      source().send(builder);
   }
}
