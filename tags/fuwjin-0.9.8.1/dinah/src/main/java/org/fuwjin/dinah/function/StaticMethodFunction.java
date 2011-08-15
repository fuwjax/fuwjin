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
import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.signature.VarArgsSignature;

/**
 * Function for static method invocation.
 */
public class StaticMethodFunction extends MemberFunction<Method> {
   /**
    * Creates a new instance.
    * @param adapter the type converter
    * @param category the function category
    * @param method the method to invoke
    */
   public StaticMethodFunction(final Adapter adapter, final Type category, final Method method) {
      super(method, VarArgsSignature.newSignature(adapter, category, method.getName(), method.getReturnType(),
            method.getParameterTypes(), method.isVarArgs()));
   }

   @Override
   protected Object invokeSafe(final Object... args) throws IllegalArgumentException, IllegalAccessException,
         InvocationTargetException {
      final Object result = member().invoke(null, args);
      if(member().getReturnType().equals(void.class)) {
         return Adapter.UNSET;
      }
      return result;
   }
}
