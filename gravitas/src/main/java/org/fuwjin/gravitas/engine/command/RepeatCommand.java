package org.fuwjin.gravitas.engine.command;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fuwjin.gravitas.engine.ExecutionEngine.EXEC_IMMEDIATELY;

import java.util.concurrent.TimeUnit;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.Inject;

public class RepeatCommand extends Command{
   private long rate;
   private TimeUnit unit = SECONDS;
   private Command command;
   @Inject
   private ExecutionEngine engine;

   @Override
   public void doRun(){
      engine.execute(source(), command, EXEC_IMMEDIATELY, rate, unit);
      source().send(String.format("Scheduling %s every %d %s", command, rate, unit.toString().toLowerCase()));
   }
   
   void unit(final String name){
      String upper = name.toUpperCase();
      if(!upper.endsWith("S")){
         upper = upper + "S";
      }
      unit = TimeUnit.valueOf(upper);
   }
}
