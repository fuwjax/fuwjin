package org.fuwjin.gravitas.engine;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ExecutionEngine{
   public static final int EXEC_IMMEDIATELY = 0;
   public static final int EXEC_ONCE = -1;
   @Inject
   private ScheduledExecutorService executor;

   public synchronized ScheduledFuture<?> execute(final Command command, final long delay, final long repeatEvery,
         final TimeUnit unit){
      ScheduledFuture<?> future;
      if(repeatEvery != EXEC_ONCE){
         future = executor.scheduleAtFixedRate(command, delay, repeatEvery, unit);
      }else if(delay > EXEC_IMMEDIATELY){
         future = executor.schedule(command, delay, unit);
      }else{
         future = executor.schedule(command, EXEC_IMMEDIATELY, MILLISECONDS);
      }
      return future;
   }

   public void shutdown(){
      executor.shutdown();
   }

   public ScheduledFuture<?> execute(Command command){
      return execute(command, EXEC_IMMEDIATELY, EXEC_ONCE, MILLISECONDS);
   }
}
