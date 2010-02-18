package org.fuwjin.gravitas.engine;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fuwjin.gravitas.engine.Execution.Status.Executing;
import static org.fuwjin.gravitas.engine.Execution.Status.Failed;
import static org.fuwjin.gravitas.engine.Execution.Status.Finished;
import static org.fuwjin.gravitas.engine.Execution.Status.Interrupted;
import static org.fuwjin.gravitas.engine.Execution.Status.Pending;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

public class Execution{
   private final ScheduledFuture<?> future;
   private final Command command;
   private final int id;
   public Execution(final int id, final Command command, final ScheduledFuture<?> future){
      this.id = id;
      this.future = future;
      this.command = command;
   }

   public boolean cancel(){
      return future.cancel(true);
   }

   public String desc(){
      return command.gesture().toString();
   }

   public int id(){
      return id;
   }

   public boolean isDone(){
      return future.isDone();
   }

   public Status status(){
      if(future.isDone()){
         try{
            future.get();
            return Finished;
         }catch(final ExecutionException e){
            return Failed;
         }catch(final Exception e){
            return Interrupted;
         }
      }else if(future.getDelay(MILLISECONDS) > 0){
         return Pending;
      }else{
         return Executing;
      }
   }

   public enum Status{
      Executing, Failed, Finished, Interrupted, Pending
   }

   public Throwable failure(){
      try{
         future.get();
         return null;
      }catch(ExecutionException e){
         return e.getCause();
      }catch(Exception e){
         return e;
      }
   }
}
