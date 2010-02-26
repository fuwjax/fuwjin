package org.fuwjin.gravitas.engine;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fuwjin.gravitas.engine.Command.Status.Executing;
import static org.fuwjin.gravitas.engine.Command.Status.Failed;
import static org.fuwjin.gravitas.engine.Command.Status.Finished;
import static org.fuwjin.gravitas.engine.Command.Status.Interrupted;
import static org.fuwjin.gravitas.engine.Command.Status.Pending;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.fuwjin.gravitas.gesture.Context;

public abstract class Command implements Runnable{
   public enum Status{
      Executing, Failed, Finished, Interrupted, Pending
   }

   private static final AtomicInteger idGenerator = new AtomicInteger();
   private Object gesture;
   private Context source;
   private int id = idGenerator.incrementAndGet();
   private ScheduledFuture<?> future;

   @Override
   public final void run(){
      try{
         doRun();
      }catch(final Exception e){
         System.err.println("ERROR: Could not handle " + gesture);
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   public void setGesture(final Object gesture){
      this.gesture = gesture;
   }

   public void setSource(final Context source){
      this.source = source;
   }
   
   public int id(){
      return id;
   }

   @Override
   public String toString(){
      return gesture.toString();
   }

   protected abstract void doRun() throws Exception;

   protected Context source(){
      return source;
   }

   public void setFuture(ScheduledFuture<?> future){
      this.future = future;
   }

   public String desc(){
      return String.valueOf(gesture);
   }

   public boolean cancel(){
      return future.cancel(true);
   }

   public Throwable failure(){
      try{
         future.get();
         return null;
      }catch(final ExecutionException e){
         return e.getCause();
      }catch(final Exception e){
         return e;
      }
   }

   public boolean isDone(){
      return future.isDone();
   }
   
   public boolean isPending(){
      return future.getDelay(MILLISECONDS) > 0;
   }

   public Status status(){
      if(isDone()){
         Throwable failure = failure();
         if(failure == null){
            return Finished;
         }
         if(failure instanceof CancellationException){
            return Interrupted;
         }
         return Failed;
      }else if(isPending()){
         return Pending;
      }
      return Executing;
   }
}
