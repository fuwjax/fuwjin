package org.fuwjin.gravitas.engine;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fuwjin.gravitas.engine.Execution.Status.*;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

public class Execution {
	private final ScheduledFuture<?> future;
	private final Runnable command;
	private int id;

	public enum Status{
		Interrupted,
		Finished,
		Pending,
		Executing,
		Failed
	}

	public Execution(int id, Runnable command, ScheduledFuture<?> future) {
		this.id = id;
		this.command = command;
		this.future = future;
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
		return command.toString();
	}

	public int id() {
		return id;
	}

}
