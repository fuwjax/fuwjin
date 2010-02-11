package org.fuwjin.gravitas.engine;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fuwjin.gravitas.engine.Execution.Status.*;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

public class Execution {
	private final ScheduledFuture<?> future;
	private int id;
   private Object gesture;

	public enum Status{
		Interrupted,
		Finished,
		Pending,
		Executing,
		Failed
	}

	public Execution(int id, Object gesture, ScheduledFuture<?> future) {
		this.id = id;
		this.future = future;
		this.gesture = gesture;
	}

	public Status status() {
		if(future.isDone()){
			try {
				future.get();
				return Finished;
			} catch( CancellationException e){
				return Interrupted;
			} catch (InterruptedException e) {
				return Interrupted;
			} catch (ExecutionException e) {
				return Failed;
			}
		}else if(future.getDelay(MILLISECONDS)>0){
			return Pending;
		}else{
			return Executing;
		}

	}

	public String desc() {
		return gesture.toString();
	}

	public int id() {
		return id;
	}

   public boolean isDone(){
      return future.isDone();
   }

   public boolean cancel(){
      return future.cancel(true);
   }
}
