package org.fuwjin.gravitas.engine.command;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fuwjin.gravitas.engine.ExecutionEngine.EXEC_ONCE;

import java.util.concurrent.TimeUnit;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.Inject;

public class DelayedCommand extends Command{
   private long delay;
   private TimeUnit unit = SECONDS;
   private Command command;
   @Inject
   private ExecutionEngine engine;

   @Override
   public void doRun(){
      engine.execute(source(), command, delay, EXEC_ONCE, unit);
      source().send(String.format("Scheduling %s in %d %s", command, delay, unit.toString().toLowerCase()));
   }

   void unit(final String name){
      String upper = name.toUpperCase();
      if(!upper.endsWith("S")){
         upper = upper + "S";
      }
      unit = TimeUnit.valueOf(upper);
   }
}
