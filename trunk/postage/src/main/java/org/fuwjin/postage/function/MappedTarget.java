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

import static org.fuwjin.postage.type.TypeUtils.isAssignable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.FunctionTarget;
import org.fuwjin.postage.type.TypeUtils;

public class MappedTarget implements FunctionTarget {
   public static FunctionTarget mapped(final FunctionTarget target, final Type returnType, final Type... parameters) {
      final Type realReturn = TypeUtils.union(target.returnType(), returnType);
      if(realReturn == null) {
         return null;
      }
      final Type[] realParams = new Type[parameters.length];
      int realIndex = 0;
      for(int index = 0; index < parameters.length; index++) {
         if(void.class.equals(parameters[index])) {
            realParams[index] = void.class;
            continue;
         }
         realParams[index] = TypeUtils.intersect(parameters[index], target.parameterType(realIndex++));
         if(realParams[index] == null) {
            return null;
         }
      }
      if(realIndex < target.requiredArguments()) {
         return null;
      }
      return new MappedTarget(target, realReturn, realIndex, realParams);
   }

   private final Type[] parameters;
   private final Type returnType;
   private final FunctionTarget target;
   private final int realParams;

   private MappedTarget(final FunctionTarget target, final Type returnType, final int realParams,
         final Type[] parameters) {
      this.target = target;
      this.returnType = returnType;
      this.parameters = parameters;
      this.realParams = realParams;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final MappedTarget o = (MappedTarget)obj;
         return getClass().equals(o.getClass()) && target.equals(o.target) && Arrays.equals(parameters, o.parameters)
               && returnType.equals(o.returnType);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return target.hashCode() + Arrays.hashCode(parameters);
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      if(args.length != parameters.length) {
         return new Failure("Expected parameters %s, received %s", Failure.types(parameters), Failure.typesOf(args));
      }
      final Object[] realArgs = new Object[realParams];
      int argIndex = 0;
      for(int index = 0; index < parameters.length; index++) {
         if(void.class.equals(parameterType(index))) {
            continue;
         }
         if(!isAssignable(parameterType(index), args[index])) {
            return new Failure("Expected parameter %d to be %s, but found %s", index, parameterType(index), args[index]
                  .getClass());
         }
         realArgs[argIndex++] = args[index];
      }
      return target.invoke(realArgs);
   }

   @Override
   public Type parameterType(final int index) {
      if(index >= parameters.length) {
         return null;
      }
      return parameters[index];
   }

   @Override
   public int requiredArguments() {
      return parameters.length;
   }

   @Override
   public Type returnType() {
      return returnType;
   }
}
