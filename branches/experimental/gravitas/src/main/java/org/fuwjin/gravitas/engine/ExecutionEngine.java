package org.fuwjin.gravitas.engine;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fuwjin.gravitas.gesture.ContextHandler.initIfNull;

import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.fuwjin.gravitas.gesture.Context;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ExecutionEngine{
   public static final int EXEC_IMMEDIATELY = 0;
   public static final int EXEC_ONCE = -1;
   @Inject
   private ScheduledExecutorService executor;
   @Inject
   private Provider<LinkedBlockingDeque<Execution>> dequeProvider;
   @Inject
   private Injector injector;
   private static AtomicInteger id = new AtomicInteger();
   
   public BlockingDeque<Execution> executions(Context source){
      ExecutionContext context = source.adapt(ExecutionContext.class);
      return initIfNull(context.executions(), dequeProvider);
   }
   
   public Execution execution(Context source, int jobId){
      for(final Execution execution: executions(source)){
         if(execution.id() == jobId){
            return execution;
         }
      }
      return null;
   }

   private void storeExecution(Context source, Command command, ScheduledFuture<?> future){
      BlockingDeque<Execution> executions = executions(source);
      executions.add(new Execution(id.incrementAndGet(), command, future));
   }

   public Execution previousExecution(Context source){
      final BlockingDeque<Execution> executions = executions(source);
      Iterator<Execution> iter = executions.descendingIterator();
      iter.next();
      return iter.next();
   }
   
   private synchronized ScheduledFuture<?> execute(final Command command, final long delay, final long repeatEvery,
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

   private ScheduledFuture<?> execute(Command command){
      return execute(command, EXEC_IMMEDIATELY, EXEC_ONCE, MILLISECONDS);
   }

   public void execute(Context source, Command command){
      command.setSource(source);
      injector.injectMembers(command);
      ScheduledFuture<?> future = execute(command);
      storeExecution(source, command, future);
   }

   public void execute(Context source, Command command, long delay, long repeatEvery, TimeUnit unit){
      command.setSource(source);
      injector.injectMembers(command);
      ScheduledFuture<?> future = execute(command, delay, repeatEvery, unit);
      storeExecution(source, command, future);
   }
}
