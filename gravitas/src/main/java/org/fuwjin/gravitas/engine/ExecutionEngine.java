package org.fuwjin.gravitas.engine;

import static org.fuwjin.gravitas.util.AnnotationUtils.getAnnotation;

import java.util.concurrent.ScheduledExecutorService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ExecutionEngine{
   @Inject
   private ScheduledExecutorService executor;
   
   public void execute(Runnable command){
      RepeatExecution repeat = getAnnotation(command.getClass(),RepeatExecution.class);
      DelayedExecution delay = getAnnotation(command.getClass(),DelayedExecution.class);
      if(repeat.repeatEvery() != -1){
         executor.scheduleAtFixedRate(command, convert(delay,repeat), repeat.repeatEvery(), repeat.unit());
      }else if(repeat.waitBetween() != -1){
         executor.scheduleWithFixedDelay(command, convert(delay,repeat), repeat.waitBetween(), repeat.unit());
      }else{
         executor.schedule(command, delay.delay(), delay.unit());
      }
   }

   private long convert(DelayedExecution delay, RepeatExecution repeat){
      return repeat.unit().convert(delay.delay(), delay.unit());
   }
}
