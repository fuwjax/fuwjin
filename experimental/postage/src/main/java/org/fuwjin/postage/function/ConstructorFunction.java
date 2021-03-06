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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

public class ConstructorFunction implements FunctionTarget {
   public static FunctionTarget constructor(final Constructor<?> c) {
      final FunctionTarget target = new ConstructorFunction(c);
      if(c.isVarArgs()) {
         return new VarArgsTarget(target);
      }
      return target;
   }

   private final Constructor<?> c;

   private ConstructorFunction(final Constructor<?> c) {
      this.c = c;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ConstructorFunction o = (ConstructorFunction)obj;
         return super.equals(obj) && c.equals(o.c);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      return access(c).newInstance(args);
   }

   @Override
   public Type parameterType(final int index) {
      if(index >= requiredArguments()) {
         return null;
      }
      return c.getParameterTypes()[index];
   }

   @Override
   public String render(final boolean castArgs, final String... argRepresentations) {
      final StringBuilder builder = new StringBuilder("new ");
      builder.append(getCanonicalName(returnType())).append("(");
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
      return c.getParameterTypes().length;
   }

   @Override
   public Type returnType() {
      return c.getDeclaringClass();
   }

   @Override
   public String toString() {
      return c.toString();
   }
}
