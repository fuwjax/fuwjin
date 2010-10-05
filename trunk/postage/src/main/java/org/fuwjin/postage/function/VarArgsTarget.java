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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;
import org.fuwjin.postage.type.TypeUtils;

public class VarArgsTarget implements FunctionTarget {
   private final FunctionTarget target;

   public VarArgsTarget(final FunctionTarget target) {
      this.target = target;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final VarArgsTarget o = (VarArgsTarget)obj;
         return getClass().equals(o.getClass()) && target.equals(o.target);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return target.hashCode();
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      final Object[] realArgs = new Object[target.requiredArguments()];
      System.arraycopy(args, 0, realArgs, 0, requiredArguments());
      final Object[] varArgs = new Object[args.length - requiredArguments()];
      System.arraycopy(args, requiredArguments(), varArgs, 0, varArgs.length);
      realArgs[requiredArguments()] = varArgs;
      return target.invoke(realArgs);
   }

   @Override
   public Type parameterType(final int index) {
      if(index >= requiredArguments()) {
         return TypeUtils.getComponentType(target.parameterType(requiredArguments()));
      }
      return target.parameterType(index);
   }

   @Override
   public int requiredArguments() {
      return target.requiredArguments() - 1;
   }

   @Override
   public Type returnType() {
      return target.returnType();
   }
}
