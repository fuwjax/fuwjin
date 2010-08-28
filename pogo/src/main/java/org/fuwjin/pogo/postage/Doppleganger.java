package org.fuwjin.pogo.postage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.fuwjin.postage.Adaptable;

public class Doppleganger implements InvocationHandler, Adaptable {
   public static <T> T content(final Object partial) {
      final Doppleganger handler = (Doppleganger)Proxy.getInvocationHandler(partial);
      return (T)handler.value;
   }

   public static <T> T create(final Object value, final Class<T> type) {
      return (T)new Doppleganger(value).as(type);
   }

   private final Object value;

   public Doppleganger(final Object value) {
      this.value = value;
   }

   @Override
   public Object as(final Class<?> type) {
      if(Doppleganger.class.isAssignableFrom(type)) {
         return this;
      }
      return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, this);
   }

   @Override
   public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
      return method.invoke(value, args);
   }
}
