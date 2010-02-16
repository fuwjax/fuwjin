package org.fuwjin.gravitas.engine.command;

import static org.fuwjin.gravitas.engine.ExecutionEngine.execution;
import static org.fuwjin.gravitas.engine.ExecutionEngine.executions;

import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.gesture.Context;

import com.google.inject.Inject;

public class SingleStatusCommand implements Runnable{
   private int jobId;
   @Inject
   private Context source;

   @Override
   public void run(){
      final StringBuilder builder = new StringBuilder();
      Execution execution = null;
      if(jobId == 0){
         final Iterable<Execution> executions = executions(source);
         Execution last = null;
         for(final Execution current: executions){
            execution = last;
            last = current;
         }
      }else{
         execution = execution(source,jobId);
      }
      if(execution == null){
         builder.append("There is no job " + jobId);
      }else{
         builder.append(execution.id()).append(") [").append(execution.status()).append("] ").append(execution.desc());
      }
      source.send(builder);
   }
}
