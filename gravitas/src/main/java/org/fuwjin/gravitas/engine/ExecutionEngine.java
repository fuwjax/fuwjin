package org.fuwjin.gravitas.engine;

import static java.util.Collections.unmodifiableCollection;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fuwjin.util.ClassUtils.getAnnotation;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ExecutionEngine{
   public static final int DO_NOT_DELAY = 0;
   public static final int DO_NOT_REPEAT_EVERY = -1;
   public static final int DO_NOT_WAIT_BETWEEN = -1;
   private final LinkedBlockingDeque<Execution> executions = new LinkedBlockingDeque<Execution>();
   @Inject
   private ScheduledExecutorService executor;
   private final AtomicInteger idGenerator = new AtomicInteger();

   public synchronized int clear(){
      int count = 0;
      final Iterator<Execution> iter = executions.iterator();
      while(iter.hasNext()){
         if(iter.next().isDone()){
            iter.remove();
            count++;
         }
      }
      return count;
   }

   public Execution execute(final Object gesture, final Runnable command){
      final RepeatExecution repeat = getAnnotation(command.getClass(), RepeatExecution.class);
      final DelayedExecution delay = getAnnotation(command.getClass(), DelayedExecution.class);
      return execute(gesture, command, convert(delay, repeat), repeat.repeatEvery(), repeat.waitBetween(), repeat
            .unit());
   }

   public synchronized Execution execute(final Object gesture, final Runnable command, final long delay, final long repeatEvery,
         final long waitBetween, final TimeUnit unit){
      ScheduledFuture<?> future;
      if(repeatEvery != DO_NOT_REPEAT_EVERY){
         future = executor.scheduleAtFixedRate(command, delay, repeatEvery, unit);
      }else if(waitBetween != DO_NOT_WAIT_BETWEEN){
         future = executor.scheduleWithFixedDelay(command, delay, waitBetween, unit);
      }else if(delay > DO_NOT_DELAY){
         future = executor.schedule(command, delay, unit);
      }else{
         future = executor.schedule(command, DO_NOT_DELAY, MILLISECONDS);
      }
      final Execution execution = new Execution(idGenerator.incrementAndGet(), gesture, future);
      executions.add(execution);
      return execution;
   }

   public synchronized Execution execution(final int executionId){
      for(final Execution execution: executions){
         if(execution.id() == executionId){
            return execution;
         }
      }
      return null;
   }

   public synchronized Iterable<Execution> executions(){
      return unmodifiableCollection(executions);
   }

   public void shutdown(){
      executor.shutdown();
   }

   private long convert(final DelayedExecution delay, final RepeatExecution repeat){
      return repeat.unit().convert(delay.delay(), delay.unit());
   }
}
