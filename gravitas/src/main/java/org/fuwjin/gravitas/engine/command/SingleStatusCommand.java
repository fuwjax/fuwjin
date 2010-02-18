package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionContextHelper;

import com.google.inject.Inject;

public class SingleStatusCommand extends Command{
   private int jobId;
   @Inject
   private ExecutionContextHelper helper;

   @Override
   public void doRun(){
      final StringBuilder builder = new StringBuilder();
      Execution execution = null;
      if(jobId == 0){
         execution = helper.previousExecution(source());
      }else{
         execution = helper.execution(source(),jobId);
      }
      if(execution == null){
         builder.append("There is no job " + jobId);
      }else{
         builder.append(execution.id()).append(") [").append(execution.status()).append("] ").append(execution.desc());
      }
      source().send(builder);
   }
}
