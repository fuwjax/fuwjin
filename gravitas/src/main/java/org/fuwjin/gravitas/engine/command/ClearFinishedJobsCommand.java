package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class ClearFinishedJobsCommand implements Runnable{
	@Inject
	private ExecutionEngine engine;
	@Inject
	private Integration source;
	
	@Override
	public void run() {
		int removed = engine.clear();
		if(removed == 0){
		   source.notify("No jobs to remove");
		}else if(removed == 1){
		   source.notify("Removed 1 job");
		}else{
		   source.notify(String.format("Removed %d jobs",removed));
		}
		
	}
}
