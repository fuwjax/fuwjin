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
import java.lang.reflect.Member;
import java.lang.reflect.Type;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.util.Adapter;
import org.fuwjin.util.ArrayUtils;
import org.fuwjin.util.BusinessException;
import org.fuwjin.util.TypeUtils;

public class VarArgsFunction extends AbstractFunction {
   private final FixedArgsFunction function;

   public VarArgsFunction(final FixedArgsFunction function) {
      super(function.name(), ArrayUtils.slice(function.argTypes(), 0, -1));
      this.function = function;
   }

   @Override
   public Object invoke(final Object... args) throws Exception {
      if(args.length < argCount()) {
         throw new FunctionInvocationException("%s expects at least %d args not %d", name(), argCount(), args.length);
      }
      final Object[] realArgs = new Object[function.argCount()];
      for(int i = 0; i < realArgs.length - 1; i++) {
         realArgs[i] = Adapter.adapt(args[i], argType(i));
      }
      final Type type = TypeUtils.getComponentType(function.argType(argCount()));
      if(args.length == function.argCount()
            && TypeUtils.isAssignableFrom(type, args[argCount()].getClass().getComponentType())) {
         try {
            realArgs[argCount()] = Adapter.adapt(args[argCount()], function.argType(argCount()));
            return function.invokeSafe(realArgs);
         } catch(final BusinessException e) {
            // continue
         }
      }
      final int varArgsLen = args.length - argCount();
      final Object varArgs = TypeUtils.newArrayInstance(type, varArgsLen);
      realArgs[argCount()] = varArgs;
      for(int i = 0; i < varArgsLen; i++) {
         Array.set(varArgs, i, Adapter.adapt(args[i + argCount()], type));
      }
      return function.invokeSafe(realArgs);
   }

   @Override
   protected Member member() {
      return function.member();
   }

   @Override
   public Function restrict(final FunctionSignature signature) {
      if(signature.argCount() < argCount()) {
         return new FailFunction(name(), "Arg count mismatch");
      }
      for(int index = 0; index < argCount(); index++) {
         if(!TypeUtils.isAssignableFrom(argType(index), signature.argType(index))) {
            return new FailFunction(name(), "Arg mismatch");
         }
      }
      if(signature.argCount() == function.argCount()
            && TypeUtils.isAssignableFrom(function.argType(argCount()), signature.argType(argCount()))) {
         return this;
      }
      final Type componentType = TypeUtils.getComponentType(function.argType(argCount()));
      for(int index = argCount(); index < signature.argCount(); index++) {
         if(!TypeUtils.isAssignableFrom(componentType, signature.argType(index))) {
            return new FailFunction(name(), "Arg mismatch");
         }
      }
      return this;
   }
}
