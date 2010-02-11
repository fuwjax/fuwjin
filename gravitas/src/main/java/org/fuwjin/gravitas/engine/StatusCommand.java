package org.fuwjin.gravitas.engine;

import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class StatusCommand implements Runnable {
	@Inject
	private ExecutionEngine engine;
	@Inject
	private Integration source;
	
	@Override
	public void run() {
		StringBuilder builder = new StringBuilder();
		for(Execution execution: engine.executions()){
			builder.append(execution.id())
				.append(") [")
				.append(execution.status())
				.append("] ")
				.append(execution.desc())
				.append("\n");
		}
		source.notify(builder);
	}
	
	@Override
	public String toString() {
		return "status";
	}
}
