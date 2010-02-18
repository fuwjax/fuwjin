package org.fuwjin.gravitas.logging;

public class LoggingGesture {
	
	private String message;
	private String logName;

	public LoggingGesture(String message, String logName) {
		this.message = message;
		this.logName = logName;
	}

	public String message() {
		return message;
	}
	
	public String logName() {
		return this.logName;
	}

}
