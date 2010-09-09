package org.fuwjin.buffer;

import static org.fuwjin.buffer.BufferFactory.newProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BufferHandler implements InvocationHandler, Buffer<Object> {
   private static Class<?> getBufferedType(final Class<? extends Buffer<?>> type) {
      for(final Type gi: type.getGenericInterfaces()) {
         if(!(gi instanceof ParameterizedType)) {
            continue;
         }
         final ParameterizedType pt = (ParameterizedType)gi;
         if(Buffer.class.isAssignableFrom((Class<?>)pt.getRawType())) {
            return (Class<?>)pt.getActualTypeArguments()[0];
         }
      }
      throw new IllegalStateException(type + " must implement " + Buffer.class);
   }

   private final Class<?> bufferedType;
   private final BufferedStorage storage;
   private final Class<? extends Buffer<?>> type;

   public BufferHandler(final Class<? extends Buffer<?>> type, final BufferedStorage storage) {
      this.type = type;
      bufferedType = getBufferedType(type);
      this.storage = storage;
   }

   @Override
   public void expect(final int batchSize) {
      storage.expect(batchSize);
   }

   @Override
   public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
      if(method.getDeclaringClass().equals(type)) {
         final Invocation invocation = storage.next(method);
         if(method.getReturnType().equals(Invocation.class)) {
            return invocation;
         }
         assert invocation.argCount() == 1;
         return invocation.arg(0);
      }
      return method.invoke(this, args);
   }

   @Override
   public Object newInterceptor() {
      return newInterceptor(null);
   }

   @Override
   public Object newInterceptor(final Object target) {
      return newProxy(bufferedType, new BufferedHandler(storage, target));
   }
}
