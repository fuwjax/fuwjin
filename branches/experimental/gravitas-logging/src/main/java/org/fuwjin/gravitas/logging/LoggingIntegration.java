package org.fuwjin.gravitas.logging;

import org.fuwjin.gravitas.gesture.Integration;

public class LoggingIntegration implements Integration {

	@Override
	public String name() {
		return "Logging";
	}

	@Override
	public void send(Object... arg0) {
		throw new RuntimeException("Not Yet Implemented");
	}

}
