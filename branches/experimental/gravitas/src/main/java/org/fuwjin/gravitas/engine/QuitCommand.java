package org.fuwjin.gravitas.engine;

import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class QuitCommand implements Runnable{
	@Inject
	private ExecutionEngine engine;
	@Inject
	private Integration source;
	
	@Override
	public void run() {
		source.notify("Shutting down now");
		engine.shutdown();
	}
}
