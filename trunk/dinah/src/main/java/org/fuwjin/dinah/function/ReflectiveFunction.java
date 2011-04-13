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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public abstract class ReflectiveFunction extends FixedArgsFunction {
   public ReflectiveFunction(final String name, final Type... argTypes) {
      super(name, argTypes);
   }

   protected abstract Object invokeImpl(Object[] args) throws InvocationTargetException, Exception;

   @Override
   protected Object invokeSafe(final Object[] args) throws Exception {
      try {
         final Object value = invokeImpl(args);
         return value;
      } catch(final InvocationTargetException e) {
         if(e.getCause() instanceof Exception) {
            throw (Exception)e.getCause();
         }
         throw new Error(e.getCause());
      } catch(final Exception e) {
         throw new FunctionInvocationException(e, "Arguments did not match %s", name());
      }
   }
}
