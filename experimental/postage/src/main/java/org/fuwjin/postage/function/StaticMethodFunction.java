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
import static org.fuwjin.postage.type.TypeUtils.getCanonicalName;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

public class StaticMethodFunction implements FunctionTarget {
   public static FunctionTarget method(final Method method) {
      final FunctionTarget target = new StaticMethodFunction(method);
      if(method.isVarArgs()) {
         return new VarArgsTarget(target);
      }
      return target;
   }

   private final Method method;

   private StaticMethodFunction(final Method method) {
      this.method = method;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final StaticMethodFunction o = (StaticMethodFunction)obj;
         return super.equals(obj) && method.equals(o.method);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      return access(method).invoke(null, args);
   }

   @Override
   public Type parameterType(final int index) {
      if(index < requiredArguments()) {
         return method.getParameterTypes()[index];
      }
      return null;
   }

   @Override
   public String render(final boolean castArgs, final String... argRepresentations) {
      final StringBuilder builder = new StringBuilder();
      builder.append(method.getDeclaringClass().getCanonicalName()).append(".").append(method.getName()).append("(");
      for(int i = 0; i < argRepresentations.length; i++) {
         if(i > 0) {
            builder.append(", ");
         }
         if(castArgs) {
            builder.append("(").append(getCanonicalName(parameterType(i))).append(")");
         }
         builder.append(argRepresentations[i]);
      }
      return builder.append(")").toString();
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
