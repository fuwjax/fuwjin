package org.fuwjin.gravitas.engine;

import static java.util.Collections.unmodifiableCollection;
import static org.fuwjin.gravitas.util.AnnotationUtils.getAnnotation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ExecutionEngine{
   @Inject
   private ScheduledExecutorService executor;
   private BlockingQueue<ScheduledFuture<?>> executions = new LinkedBlockingQueue<ScheduledFuture<?>>();
   
   public void execute(Runnable command){
      RepeatExecution repeat = getAnnotation(command.getClass(),RepeatExecution.class);
      DelayedExecution delay = getAnnotation(command.getClass(),DelayedExecution.class);
      ScheduledFuture<?> future;
      if(repeat.repeatEvery() != -1){
         future = executor.scheduleAtFixedRate(command, convert(delay,repeat), repeat.repeatEvery(), repeat.unit());
      }else if(repeat.waitBetween() != -1){
         future = executor.scheduleWithFixedDelay(command, convert(delay,repeat), repeat.waitBetween(), repeat.unit());
      }else{
         future = executor.schedule(command, delay.delay(), delay.unit());
      }
      executions.add(future);
   }
   
   public Iterable<ScheduledFuture<?>> executions(){
	   return unmodifiableCollection(executions);
   }
   
   public void shutdown(){
	   executor.shutdown();
   }

   private long convert(DelayedExecution delay, RepeatExecution repeat){
      return repeat.unit().convert(delay.delay(), delay.unit());
   }
}
