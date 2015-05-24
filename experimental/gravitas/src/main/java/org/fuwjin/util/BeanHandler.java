/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BeanHandler implements InvocationHandler{
   private final ConcurrentMap<String, Object> map = new ConcurrentHashMap<String, Object>();

   @Override
   public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable{
      if(Object.class.equals(method.getDeclaringClass())){
         return method.invoke(this, args);
      }
      if(args == null || args.length == 0){
         return map.get(method.getName());
      }
      if(args.length == 1){
         map.put(method.getName(), args[0]);
         return this;
      }
      throw new UnsupportedOperationException();
   }
}