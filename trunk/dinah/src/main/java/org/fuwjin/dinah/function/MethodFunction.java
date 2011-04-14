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
import org.fuwjin.util.Adapter;
import org.fuwjin.util.ArrayUtils;

/**
 * Function for reflective method invocation.
 */
public class MethodFunction extends FixedArgsFunction {
   private final Method method;

   /**
    * Creates a new instance.
    * @param category the function category
    * @param method the method to invoke
    * @param type the host object type
    */
   public MethodFunction(final String category, final Method method, final Type type) {
      super(category + '.' + method.getName(), ArrayUtils.push(method.getParameterTypes(), type));
      this.method = method;
   }

   @Override
   protected Object invokeSafe(final Object... args) throws IllegalArgumentException, IllegalAccessException,
         InvocationTargetException {
      final Object obj = args[0];
      final Object[] realArgs = new Object[args.length - 1];
      System.arraycopy(args, 1, realArgs, 0, args.length - 1);
      final Object result = method.invoke(obj, realArgs);
      if(method.getReturnType().equals(void.class)) {
         return Adapter.unset();
      }
      return result;
   }

   @Override
   protected Member member() {
      return method;
   }
}
