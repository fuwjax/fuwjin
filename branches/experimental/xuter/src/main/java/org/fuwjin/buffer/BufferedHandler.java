package org.fuwjin.buffer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BufferedHandler implements InvocationHandler {
   private final BufferedStorage storage;
   private final Object target;

   public BufferedHandler(final BufferedStorage storage, final Object target) {
      this.storage = storage;
      this.target = target;
   }

   @Override
   public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
      Object ret = null;
      Throwable ex = null;
      if(target != null) {
         try {
            ret = method.invoke(target, args);
         } catch(final InvocationTargetException e) {
            ex = e.getCause();
         } catch(final Throwable t) {
            ex = t;
         }
      }
      storage.store(method, new Invocation(args, ret, ex));
      if(ex != null) {
         throw ex;
      }
      return ret;
   }
}
