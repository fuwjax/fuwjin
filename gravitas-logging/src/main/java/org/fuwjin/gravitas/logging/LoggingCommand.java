package org.fuwjin.gravitas.logging;

import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class LoggingCommand implements Runnable {
	private String message;
	@Inject
	@Log
	private Integration destination;
	
	public LoggingCommand(String message) {
		this.message = message;
	}

	@Override
	public void run() {
		System.out.println("Logging");
		destination.send(message);
	}

}
