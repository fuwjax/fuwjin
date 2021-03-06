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

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.FunctionTarget;

public class MethodFunction implements FunctionTarget {
   public static FunctionTarget method(final Method method, final Class<?> firstParam) {
      final FunctionTarget target = new MethodFunction(method, firstParam);
      if(method.isVarArgs()) {
         return new VarArgsTarget(target);
      }
      return target;
   }

   private final Method method;
   private final Class<?> firstParam;

   private MethodFunction(final Method method, final Class<?> firstParam) {
      this.method = method;
      this.firstParam = firstParam;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final MethodFunction o = (MethodFunction)obj;
         return super.equals(obj) && method.equals(o.method)
               && (firstParam == null ? o.firstParam == null : firstParam.equals(o.firstParam));
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      final Object[] trim = new Object[args.length - 1];
      System.arraycopy(args, 1, trim, 0, trim.length);
      final Object object = args[0];
      if(!firstParam.isInstance(object)) {
         return new Failure("Target must be %s, not %s", firstParam, object.getClass());
      }
      return access(method).invoke(object, trim);
   }

   @Override
   public Type parameterType(final int index) {
      if(index == 0) {
         return firstParam;
      }
      if(index < requiredArguments()) {
         return method.getParameterTypes()[index - 1];
      }
      return null;
   }

   @Override
   public String render(final boolean castArgs, final String... argRepresentations) {
      final StringBuilder builder = new StringBuilder();
      if(castArgs) {
         builder.append("((").append(getCanonicalName(parameterType(0))).append(")").append(argRepresentations[0])
               .append(")");
      } else {
         builder.append(argRepresentations[0]);
      }
      builder.append(".").append(method.getName()).append("(");
      for(int i = 1; i < argRepresentations.length; i++) {
         if(i > 1) {
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
      return method.getParameterTypes().length + 1;
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
