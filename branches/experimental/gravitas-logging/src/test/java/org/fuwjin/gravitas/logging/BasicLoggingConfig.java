package org.fuwjin.gravitas.logging;

import static org.fuwjin.gravitas.Gravitas.startGravitas;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.Integration;
import org.junit.Assert;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;

public class BasicLoggingConfig {
	@Inject
	@Log
	private Integration logIntegration;

	@Test
	public void logMessagesSentToIntegration() throws Exception {
		List<Module> modules = Collections.<Module>singletonList(new TestModule());
		Injector injector = startGravitas(modules);
		injector.injectMembers(this);
		LogTestIntegration testLog = (LogTestIntegration) logIntegration;
		testLog.init();
		testLog.logFoo();
		testLog.expect("Foo");
		testLog.logBar("123");
		testLog.expect("Bar 123");
	}
	
	public class TestModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(LogTestIntegration.class).asEagerSingleton();
		}

		@Provides
		TestLog getLogger(EventRouter router) {
			return LoggerFactory.createLogger(TestLog.class, router);
		}
	}
	
	static class LogTestIntegration implements Integration {
		@Inject
		private TestLog log;
		@Inject
		EventRouter router;
		
		private BlockingQueue<String> output = new LinkedBlockingQueue<String>();
		
		public void init() {
			//must register the integration with the event router
			router.raise(this, "Howdy");
		}
		
		public void logFoo() {
			log.foo();
		}
		
		public void logBar(String arg) {
			log.bar(arg);
		}
		
		public void expect(String message) throws InterruptedException {
			String actual = output.take();
			assertEquals(message, actual);
		}
		
		@Override
		public String name() {
			return "Log Test";
		}

		@Override
		public void send(Object... messages) {
			Assert.assertEquals(1, messages.length);
			output.add((String) messages[0]);
		}
	}
	
	static interface TestLog {
		@LogStatement(format="Foo", logName = "Log Test")
		public void foo();
		
		@LogStatement(format="Bar %s", logName = "Log Test")
		public void bar(String arg);
	}
}
