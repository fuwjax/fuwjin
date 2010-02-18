package org.fuwjin.gravitas.engine.command;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.ExecutionContextHelper;
import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Context;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class DelayedCommand extends Command{
   private long delay;
   private TimeUnit unit = SECONDS;
   private Command command;
   @Inject
   private ExecutionEngine engine;
   @Inject
   private ExecutionContextHelper helper;

   @Override
   public void doRun(){
      ScheduledFuture<?> future = engine.execute(command, delay, -1, -1, unit);
      helper.storeExecution(source(), command, future);
      source().send(String.format("Scheduling %s in %d %s", command, delay, unit.toString().toLowerCase()));
   }

   void unit(String name){
      String upper = name.toUpperCase();
      if(!upper.endsWith("S")){
         upper = upper+"S";
      }
      unit = TimeUnit.valueOf(upper);
   }
   
   @Override
   public void setSource(Context source){
      super.setSource(source);
      command.setSource(source);
   }

   @Override
   public void inject(Injector injector){
      super.inject(injector);
      command.inject(injector);
   }
}
