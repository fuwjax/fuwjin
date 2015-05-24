/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.postage.function;

import static org.fuwjin.postage.type.ObjectUtils.access;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

public class InstanceMethodFunction implements FunctionTarget {
   public static FunctionTarget method(final Method method, final Object object) {
      final FunctionTarget target = new InstanceMethodFunction(method, object);
      if(method.isVarArgs()) {
         return new VarArgsTarget(target);
      }
      return target;
   }

   private final Method method;
   private final Object target;

   private InstanceMethodFunction(final Method method, final Object target) {
      this.method = method;
      this.target = target;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceMethodFunction o = (InstanceMethodFunction)obj;
         return super.equals(obj) && method.equals(o.method)
               && (target == null ? o.target == null : target.equals(o.target));
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      return access(method).invoke(target, args);
   }

   @Override
   public Type parameterType(final int index) {
      if(index >= requiredArguments()) {
         return null;
      }
      return method.getParameterTypes()[index];
   }

   @Override
   public String render(final boolean castArgs, final String... argRepresentations) {
      throw new UnsupportedOperationException();
   }

   @Override
   public int requiredArguments() {
      return method.getParameterTypes().length;
   }

   @Override
   public Type returnType() {
      return method.getReturnType();
   }

   @Override
   public String toString() {
      return method.toString();
   }
}
