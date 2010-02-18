/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.gravitas.gesture;

import static org.fuwjin.util.ClassUtils.newProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.inject.Provider;

public class ContextHandler implements InvocationHandler{
   private static ThreadLocal<Method> lastMethod = new ThreadLocal<Method>();
   private static ThreadLocal<ContextHandler> lastHandler = new ThreadLocal<ContextHandler>();
   
   public static <T> T initIfNull(T proxyMethod, Provider<? extends T> provider){
      ContextHandler handler = lastHandler.get();
      return (T)handler.initBy(lastMethod.get(), provider);
   }
   
   private ConcurrentMap<String, Object> map = new ConcurrentHashMap<String, Object>();
   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
      lastHandler.set(this);
      lastMethod.set(method);
      String name = name(method);
      Object value = map.get(name);
      if(value == null && method.getReturnType().isInterface()){
         value = newProxy(method.getReturnType(), this);
         map.put(name, value);
      }
      return value;
   }
   
   private String name(Class<?> type, String property){
      return type.getSimpleName()+'.'+property;
   }
   
   private String name(Method method){
      return name(method.getDeclaringClass(),method.getName());
   }
   
   Object initBy(Method method, Provider<?> provider){
      String name = name(method);
      Object value = map.get(name);
      if(value == null){
         value = provider.get();
         Object old = map.putIfAbsent(name, value);
         if(old != null){
            value = old;
         }
      }
      return value;
   }
}