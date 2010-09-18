package org.fuwjin.pogo.postage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.fuwjin.postage.Adaptable;

/**
 * A horrible class which unfortunately is needed because much of pogo was
 * written before postage, and therefore doesn't play by the new rules as well
 * as it should. This class should be refactored away.
 */
public class Doppleganger implements InvocationHandler, Adaptable {
   /**
    * Returns the content from the doppleganger.
    * @param <T> the expected content type
    * @param dopple the doppleganger
    * @return the doppleganger's content
    * @throws IllegalArgumentException if dopple is not a Doppleganger
    */
   public static <T> T content(final Object dopple) {
      final Doppleganger handler = (Doppleganger)Proxy.getInvocationHandler(dopple);
      return (T)handler.value;
   }

   /**
    * Creates a new doppleganger.
    * @param <T> the doppleganger's pretend type
    * @param value the content of the doppleganger
    * @param type the doppleganger's pretend type
    * @return the doppleganger
    */
   public static <T> T create(final Object value, final Class<T> type) {
      return (T)new Doppleganger(value).as(type);
   }

   private final Object value;

   /**
    * Creates a new doppleganger handler.
    * @param value the content of the doppleganger
    */
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
