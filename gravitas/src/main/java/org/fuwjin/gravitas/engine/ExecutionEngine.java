package org.fuwjin.gravitas.engine;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ExecutionEngine{
   public static final int DO_NOT_DELAY = 0;
   public static final int DO_NOT_REPEAT_EVERY = -1;
   public static final int DO_NOT_WAIT_BETWEEN = -1;
   @Inject
   private ScheduledExecutorService executor;

   public synchronized ScheduledFuture<?> execute(final Command command, final long delay, final long repeatEvery,
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
      return future;
   }

   public void shutdown(){
      executor.shutdown();
   }

   public ScheduledFuture<?> execute(Command command){
      return execute(command, DO_NOT_DELAY, DO_NOT_REPEAT_EVERY, DO_NOT_WAIT_BETWEEN, MILLISECONDS);
   }
}
