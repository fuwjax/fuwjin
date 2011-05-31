/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.dinah.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.fuwjin.dinah.Adapter;

/**
 * Function for reflective method invocation.
 */
public class InstanceMethodFunction extends FixedArgsFunction<Method> {
   private final Object target;

   /**
    * Creates a new instance.
    * @param adapter the type converter
    * @param category the function category
    * @param method the method to invoke
    * @param target the host object type
    */
   public InstanceMethodFunction(final Adapter adapter, final String category, final Method method, final Object target) {
      super(adapter, method, category + '.' + method.getName(), method.getParameterTypes());
      this.target = target;
   }

   @Override
   protected Object invokeSafe(final Object... args) throws IllegalArgumentException, IllegalAccessException,
         InvocationTargetException {
      final Object result = member().invoke(target, args);
      if(member().getReturnType().equals(void.class)) {
         return Adapter.UNSET;
      }
      return result;
   }
}
