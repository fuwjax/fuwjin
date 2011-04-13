/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.dinah.function;

import java.lang.reflect.Member;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;

public class FailFunction extends AbstractFunction{
   private final String pattern;
   private final Object[] alertArgs;
   private final Throwable cause;

   public FailFunction(final String name, final String pattern, final Object... args){
      super(name);
      this.pattern = pattern;
      alertArgs = args;
      cause = null;
   }

   public FailFunction(final String name, final Throwable cause, final String pattern, final Object... args){
      super(name);
      this.cause = cause;
      this.pattern = pattern;
      alertArgs = args;
   }

   @Override
   public Object invoke(final Object... args) throws FunctionInvocationException{
      if(cause == null){
         throw new FunctionInvocationException(pattern, alertArgs);
      }
      throw new FunctionInvocationException(cause, pattern, alertArgs);
   }

   @Override
   protected Member member(){
      return null;
   }

   @Override
   public Function restrict(final FunctionSignature signature){
      return this;
   }
}
