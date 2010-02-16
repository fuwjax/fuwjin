package org.fuwjin.gravitas.engine.command;

import static org.fuwjin.util.StringUtils.join;

import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Context;

import com.google.inject.Inject;

public class StatusCommand implements Runnable{
   @Inject
   private Context source;

   @Override
   public void run(){
      final StringBuilder builder = new StringBuilder();
      final Object separator = join("\n");
      for(final Execution execution: ExecutionEngine.executions(source)){
         builder.append(separator).append(execution.id()).append(") [").append(execution.status()).append("] ").append(
               execution.desc());
      }
      source.send(builder);
   }
}
