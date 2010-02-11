package org.fuwjin.gravitas.engine;

import static java.util.Collections.unmodifiableCollection;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fuwjin.gravitas.util.ClassUtils.getAnnotation;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ExecutionEngine{
   public static final int DO_NOT_REPEAT_EVERY = -1;
   public static final int DO_NOT_WAIT_BETWEEN = -1;
   public static final int DO_NOT_DELAY = 0;
   
   @Inject
   private ScheduledExecutorService executor;
   private BlockingQueue<Execution> executions = new LinkedBlockingQueue<Execution>();
   private AtomicInteger idGenerator = new AtomicInteger();
   
   public Execution execute(Object gesture, Runnable command){
      RepeatExecution repeat = getAnnotation(command.getClass(),RepeatExecution.class);
      DelayedExecution delay = getAnnotation(command.getClass(),DelayedExecution.class);
      return execute(gesture, command, convert(delay, repeat), repeat.repeatEvery(), repeat.waitBetween(), repeat.unit());
   }
   
   public Iterable<Execution> executions(){
	   return unmodifiableCollection(executions);
   }
   
   public void shutdown(){
	   executor.shutdown();
   }

   private long convert(DelayedExecution delay, RepeatExecution repeat){
      return repeat.unit().convert(delay.delay(), delay.unit());
   }

   public Execution execution(int executionId){
      for(Execution execution: executions){
         if(execution.id() == executionId){
            return execution;
         }
      }
      return null;
   }

   public int clear(){
      int count = 0;
      Iterator<Execution> iter = executions.iterator();
      while(iter.hasNext()){
         if(iter.next().isDone()){
            iter.remove();
            count++;
         }
      }
      return count;
   }

   public Execution execute(Object gesture, Runnable command, long delay, long repeatEvery, long waitBetween, TimeUnit unit){
      ScheduledFuture<?> future;
      if(repeatEvery != DO_NOT_REPEAT_EVERY){
         future = executor.scheduleAtFixedRate(command, delay, repeatEvery, unit);
      }else if(waitBetween != DO_NOT_WAIT_BETWEEN){
         future = executor.scheduleWithFixedDelay(command, delay, waitBetween, unit);
      }else if(delay>DO_NOT_DELAY){
         future = executor.schedule(command, delay, unit);
      }else{
         future = executor.schedule(command, DO_NOT_DELAY, MILLISECONDS);
      }
      Execution execution = new Execution(idGenerator.incrementAndGet(),gesture,future);
      executions.add(execution);
      return execution;
   }
}
