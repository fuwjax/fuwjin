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

public class ContextHandler implements InvocationHandler{
   private static ThreadLocal<Method> lastMethod = new ThreadLocal<Method>();
   private static ThreadLocal<ContextHandler> lastHandler = new ThreadLocal<ContextHandler>();
   
   public static <T> T init(T proxyMethod, T value){
      ContextHandler handler = lastHandler.get();
      return (T)handler.init(lastMethod.get(), value);
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
   
   Object init(Method method, Object value){
      Object old = map.putIfAbsent(name(method), value);
      if(old == null){
         return value;
      }
      return old;
   }
}