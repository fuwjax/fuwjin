/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.dinah.function;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.util.Adapter;
import org.fuwjin.util.Adapter.AdaptException;
import org.fuwjin.util.ArrayUtils;
import org.fuwjin.util.TypeUtils;

/**
 * Function decorator for methods and constructors to handle var args.
 */
public class VarArgsFunction extends AbstractFunction {
   private final FixedArgsFunction<?> function;

   /**
    * Creates a new instance.
    * @param function the decorated function
    */
   public VarArgsFunction(final FixedArgsFunction<?> function) {
      super(function.name(), ArrayUtils.slice(function.argTypes(), 0, -1));
      this.function = function;
   }

   @Override
   public Object invoke(final Object... args) throws AdaptException, InvocationTargetException {
      if(args.length == function.argCount()) {
         try {
            return function.invoke(args);
         } catch(final AdaptException e) {
            // continue
         }
      }
      final Object[] realArgs = new Object[function.argCount()];
      System.arraycopy(args, 0, realArgs, 0, argCount());
      final Type type = TypeUtils.getComponentType(function.argType(argCount()));
      final int varArgsLen = args.length - argCount();
      final Object varArgs = TypeUtils.newArrayInstance(type, varArgsLen);
      realArgs[argCount()] = varArgs;
      for(int i = 0; i < varArgsLen; ++i) {
         Array.set(varArgs, i, Adapter.adapt(args[i + argCount()], type));
      }
      return function.invoke(realArgs);
   }

   @Override
   public AbstractFunction restrict(final FunctionSignature signature) {
      final AbstractFunction func = function.restrict(signature);
      if(AbstractFunction.NULL != func) {
         return func;
      }
      if(isMatch(signature)) {
         return this;
      }
      return AbstractFunction.NULL;
   }

   @Override
   protected boolean isPrivate() {
      return function.isPrivate();
   }

   private boolean isMatch(final FunctionSignature signature) {
      if(signature.argCount() < argCount()) {
         return false;
      }
      for(int index = 0; index < argCount(); ++index) {
         if(!TypeUtils.isAssignableFrom(argType(index), signature.argType(index))) {
            return false;
         }
      }
      return isVarArgsMatch(signature);
   }

   private boolean isVarArgsMatch(final FunctionSignature signature) {
      final Type componentType = TypeUtils.getComponentType(function.argType(argCount()));
      for(int index = argCount(); index < signature.argCount(); ++index) {
         if(!TypeUtils.isAssignableFrom(componentType, signature.argType(index))) {
            return false;
         }
      }
      return true;
   }
}
