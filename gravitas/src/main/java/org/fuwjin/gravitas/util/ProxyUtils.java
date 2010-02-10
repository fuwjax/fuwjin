package org.fuwjin.gravitas.util;

import static java.lang.reflect.Proxy.newProxyInstance;

import java.lang.reflect.InvocationHandler;

public class ProxyUtils{
   public static <T> T newProxy(Class<T> type, InvocationHandler handler){
      return type.cast(newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler));
   }
}
