package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class SingleStatusCommand implements Runnable {
	@Inject
	private ExecutionEngine engine;
	@Inject
	private Integration source;
	private int jobId;
	
	@Override
	public void run() {
		StringBuilder builder = new StringBuilder();
		Execution execution = engine.execution(jobId);
		if(execution == null){
		   builder.append("There is no execution "+jobId);
		}else{
			builder.append(execution.id())
				.append(") [")
				.append(execution.status())
				.append("] ")
				.append(execution.desc());
		}
		source.notify(builder);
	}
}
