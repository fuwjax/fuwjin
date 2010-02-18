package org.fuwjin.gravitas.engine;

import static org.fuwjin.gravitas.gesture.ContextHandler.initIfNull;

import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.fuwjin.gravitas.gesture.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ExecutionContextHelper{
   @Inject
   private Provider<LinkedBlockingDeque<Execution>> dequeProvider;
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

   public void storeExecution(Context source, Command command, ScheduledFuture<?> future){
      BlockingDeque<Execution> executions = executions(source);
      executions.add(new Execution(id.incrementAndGet(), command, future));
   }

   public Execution previousExecution(Context source){
      final BlockingDeque<Execution> executions = executions(source);
      Iterator<Execution> iter = executions.descendingIterator();
      iter.next();
      return iter.next();
   }
}
