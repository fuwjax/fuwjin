package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class SingleStatusCommand implements Runnable{
   @Inject
   private ExecutionEngine engine;
   private int jobId;
   @Inject
   private Integration source;

   @Override
   public void run(){
      final StringBuilder builder = new StringBuilder();
      Execution execution = null;
      if(jobId == 0){
         final Iterable<Execution> executions = engine.executions();
         Execution last = null;
         for(final Execution current: executions){
            execution = last;
            last = current;
         }
      }else{
         execution = engine.execution(jobId);
      }
      if(execution == null){
         builder.append("There is no job " + jobId);
      }else{
         builder.append(execution.id()).append(") [").append(execution.status()).append("] ").append(execution.desc());
      }
      source.send(builder);
   }
}
