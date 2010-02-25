package org.fuwjin.gravitas.engine.command;

import static org.fuwjin.util.StringUtils.join;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.Inject;

public class StatusCommand extends Command{
   @Inject
   private ExecutionEngine engine;

   @Override
   public void doRun(){
      final StringBuilder builder = new StringBuilder();
      final Object separator = join("\n");
      for(final Execution execution: engine.executions(source())){
         builder.append(separator).append(execution.id()).append(") [").append(execution.status()).append("] ").append(
               execution.desc());
      }
      source().send(builder);
   }
}
