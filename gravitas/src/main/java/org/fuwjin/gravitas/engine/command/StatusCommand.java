package org.fuwjin.gravitas.engine.command;

import static org.fuwjin.gravitas.util.StringUtils.join;

import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class StatusCommand implements Runnable{
   @Inject
   private ExecutionEngine engine;
   @Inject
   private Integration source;

   @Override
   public void run(){
      final StringBuilder builder = new StringBuilder();
      final Object separator = join("\n");
      for(final Execution execution: engine.executions()){
         builder.append(separator).append(execution.id()).append(") [").append(execution.status()).append("] ").append(
               execution.desc());
      }
      source.notify(builder);
   }
}
