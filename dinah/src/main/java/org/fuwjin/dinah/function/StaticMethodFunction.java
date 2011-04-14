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
import org.fuwjin.util.Adapter;

/**
 * Function for static method invocation.
 */
public class StaticMethodFunction extends FixedArgsFunction {
   private final Method method;

   /**
    * Creates a new instance.
    * @param category the function category
    * @param method the method to invoke
    */
   public StaticMethodFunction(final String category, final Method method) {
      super(category + '.' + method.getName(), method.getParameterTypes());
      this.method = method;
   }

   @Override
   protected Object invokeSafe(final Object... args) throws IllegalArgumentException, IllegalAccessException,
         InvocationTargetException {
      final Object result = method.invoke(null, args);
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
