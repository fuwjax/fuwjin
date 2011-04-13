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

import java.lang.reflect.Type;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.util.Adapter;
import org.fuwjin.util.TypeUtils;

public abstract class FixedArgsFunction extends AbstractFunction{
   public FixedArgsFunction(final String name, final Type... argTypes){
      super(name, argTypes);
   }

   @Override
   public Object invoke(final Object... args) throws Exception{
      if(args.length != argCount()){
         throw new FunctionInvocationException("%s expects %d args not %d", name(), argCount(), args.length);
      }
      final Object[] realArgs = new Object[args.length];
      for(int i = 0; i < args.length; i++){
         realArgs[i] = Adapter.adapt(args[i], argType(i));
      }
      return invokeSafe(realArgs);
   }

   protected abstract Object invokeSafe(Object... args) throws Exception;

   @Override
   public Function restrict(final FunctionSignature signature){
      if(signature.argCount() != argCount()){
         return new FailFunction(name(), "%s expected %d args not %d", name(), argCount(), signature.argCount());
      }
      for(int i = 0; i < argCount(); i++){
         if(!TypeUtils.isAssignableFrom(argType(i), signature.argType(i))){
            return new FailFunction(name(), "%s expected arg %d to be %s not %s", name(), i, argType(i),
                  signature.argType(i));
         }
      }
      return this;
   }
}
