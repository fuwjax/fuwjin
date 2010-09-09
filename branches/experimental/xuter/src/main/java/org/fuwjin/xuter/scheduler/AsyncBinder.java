package org.fuwjin.xuter.scheduler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.fuwjin.pogo.PogoClassLoader;

public class AsyncBinder {
   public static Method isAsync(final Method method) {
      return isVoid(method) != null && method.getGenericExceptionTypes().length == 0 ? method : null;
   }

   public static Method isVoid(final Method method) {
      return method.getGenericReturnType().equals(void.class) ? method : null;
   }

   private final Executor pool;
   private final PogoClassLoader loader;

   public AsyncBinder(final int threads) {
      pool = new ExecutorPool(threads);
      loader = new PogoClassLoader("xuter.impl.pogo");
   }

   public <T> T async(final Class<T> type, final T obj) {
      try {
         final String name = type.getPackage().getName() + "." + type.getSimpleName() + "Async";
         final Class<?> asyncType = loader.loadClass(name, type);
         final Constructor<?> c = asyncType.getConstructor(Executor.class, type);
         return type.cast(c.newInstance(new ScheduledExecutor(pool), obj));
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }
}
