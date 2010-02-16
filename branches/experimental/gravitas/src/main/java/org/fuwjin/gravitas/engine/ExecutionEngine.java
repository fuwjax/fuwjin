package org.fuwjin.gravitas.engine;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fuwjin.gravitas.gesture.ContextHandler.init;
import static org.fuwjin.util.ClassUtils.getAnnotation;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.fuwjin.gravitas.gesture.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ExecutionEngine{
   public static BlockingDeque<Execution> executions(Context source){
      ExecutionContext context = source.adapt(ExecutionContext.class);
      LinkedBlockingDeque<Execution> executions = context.executions();
      if(executions == null){
         executions = init(executions, new LinkedBlockingDeque<Execution>());
      }
      return executions;
   }
   
   public static final int DO_NOT_DELAY = 0;
   public static final int DO_NOT_REPEAT_EVERY = -1;
   public static final int DO_NOT_WAIT_BETWEEN = -1;
   @Inject
   private ScheduledExecutorService executor;
   private final AtomicInteger idGenerator = new AtomicInteger();

   public Execution execute(final Context source, final Object gesture, final Runnable command){
      final RepeatExecution repeat = getAnnotation(command.getClass(), RepeatExecution.class);
      final DelayedExecution delay = getAnnotation(command.getClass(), DelayedExecution.class);
      return execute(source, gesture, command, convert(delay, repeat), repeat.repeatEvery(), repeat.waitBetween(), repeat
            .unit());
   }

   public synchronized Execution execute(final Context source, final Object gesture, final Runnable command, final long delay, final long repeatEvery,
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
      executions(source).add(execution);
      return execution;
   }

   public void shutdown(){
      executor.shutdown();
   }

   private long convert(final DelayedExecution delay, final RepeatExecution repeat){
      return repeat.unit().convert(delay.delay(), delay.unit());
   }

   public static Execution execution(Context source, int jobId){
      for(final Execution execution: executions(source)){
         if(execution.id() == jobId){
            return execution;
         }
      }
      return null;
   }
}
