package org.fuwjin.gravitas.logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.Integration;

public class LoggerFactory {
	@SuppressWarnings("unchecked")
	public static <T> T createLogger(Class<T> logInterface, EventRouter router) {
		return (T) Proxy.newProxyInstance(logInterface.getClassLoader(), new Class[] {logInterface}, new LoggerInvocationHandler(router));
	}
	
	public static class LoggerInvocationHandler implements InvocationHandler {
		private EventRouter router;
		
		public LoggerInvocationHandler(EventRouter router) {
			this.router = router;
		}

		@Override
		public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
			LogStatement statement = method.getAnnotation(LogStatement.class);
			String message = String.format(statement.format(), args);
			Integration logDest = router.getContext(statement.logName());
			logDest.send(message);
			return null;
		}
	}
}
