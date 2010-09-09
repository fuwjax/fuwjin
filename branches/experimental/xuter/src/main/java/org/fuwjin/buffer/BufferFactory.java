package org.fuwjin.buffer;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class BufferFactory {
   public static <T extends Buffer<?>> T newBuffer(final Class<T> type) {
      return newProxy(type, new BufferHandler(type, new BufferedStorage(1, SECONDS)));
   }

   public static <T> T newProxy(final Class<T> type, final InvocationHandler handler) {
      return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler));
   }
}
