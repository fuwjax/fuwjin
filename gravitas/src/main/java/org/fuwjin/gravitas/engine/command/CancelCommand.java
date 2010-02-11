package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class CancelCommand implements Runnable {
	@Inject
	private ExecutionEngine engine;
	@Inject
	private Integration source;
	private int jobId;
	
	@Override
	public void run() {
		Execution execution = engine.execution(jobId);
		if(execution == null){
		   source.notify("There is no execution "+jobId);
		}else{
		   if(execution.cancel()){
		      source.notify("Job "+jobId+" has been cancelled");
		   }else{
		      source.notify("Could not cancel job "+jobId);
		   }
		}
	}
}
