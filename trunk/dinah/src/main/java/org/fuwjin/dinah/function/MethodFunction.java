/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.dinah.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.fuwjin.util.ArrayUtils;

public class MethodFunction extends ReflectiveFunction{
   private final Method method;

   public MethodFunction(final String typeName, final Method method, final Type type){
      super(typeName + '.' + method.getName(), ArrayUtils.prepend(type, method.getParameterTypes()));
      this.method = method;
   }

   @Override
   protected Object invokeImpl(final Object[] args) throws InvocationTargetException, Exception{
      final Object obj = args[0];
      final Object[] realArgs = new Object[args.length - 1];
      System.arraycopy(args, 1, realArgs, 0, args.length - 1);
      return method.invoke(obj, realArgs);
   }

   @Override
   protected Member member(){
      return method;
   }
}
