package org.fuwjin.pogo.parser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Partial implements InvocationHandler {
   public static <T> T content(final Object partial) {
      final Partial handler = (Partial)Proxy.getInvocationHandler(partial);
      return (T)handler.content;
   }

   private final Object content;

   public Partial(final Object value) {
      content = value;
   }

   public <T> T as(final Class<T> type) {
      return (T)Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, this);
   }

   @Override
   public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
      throw new UnsupportedOperationException();
   }
}
