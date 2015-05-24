package org.fuwjin.gravitas.logging;

import org.fuwjin.gravitas.gesture.Event;
import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.Integration;
import org.fuwjin.gravitas.gesture.handler.AbstractEventHandler;

import com.google.inject.Inject;

public class LoggingEventHandler extends AbstractEventHandler {
	private static final DefaultLogDestination DEFAULT_DEST = new DefaultLogDestination();
	@Inject
	EventRouter router;
	
	@Override
	public boolean handle(Event event) throws Exception {
		LoggingGesture gesture = (LoggingGesture) event.gesture();
		System.out.println("Handling Logging Event");
		Integration logDestination = lookupLogDestination(gesture.logName());
		logDestination.send(gesture.message());
		return true;
	}

	private Integration lookupLogDestination(String logName) {
		Integration dest = router.getContext(logName);
		if(dest == null) {
			System.err.println("WARNING: No integration named %s was found.  Gravitas Logging will use the default System.out logger.");
			return DEFAULT_DEST;
		}
		return dest;
	}
	
	private static class DefaultLogDestination implements Integration {

		@Override
		public String name() {
			return "System.out Logger";
		}

		@Override
		public void send(Object... messages) {
			for(Object message : messages) {
				System.out.println(message);
			}
		}		
	}
}
