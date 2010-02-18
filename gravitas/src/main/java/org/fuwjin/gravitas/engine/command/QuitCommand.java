package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.Inject;

public class QuitCommand extends Command{
   @Inject
   private ExecutionEngine engine;

   @Override
   public void doRun(){
      source().send("Shutting down now");
      engine.shutdown();
   }
}
