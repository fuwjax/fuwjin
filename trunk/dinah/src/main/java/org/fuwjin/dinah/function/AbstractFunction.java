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
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;

/**
 * Base implementation of Function interface.
 */
public abstract class AbstractFunction implements Function {
   private final FunctionSignature signature;

   protected AbstractFunction(final FunctionSignature signature) {
      this.signature = signature;
   }

   @Override
   public Object invoke(final Object... args) throws AdaptException, InvocationTargetException {
      try {
         return invokeSafe(signature().adapt(args));
      } catch(final IllegalAccessException e) {
         throw new AdaptException(e, "%s could not be accessed: %s", signature(), e);
      } catch(final InstantiationException e) {
         throw new AdaptException(e, "%s could not be instantiated: %s", signature(), e);
      } catch(final IllegalArgumentException e) {
         throw new AdaptException(e, "%s could not be adapted: %s", signature(), e);
      }
   }

   @Override
   public FunctionSignature signature() {
      return signature;
   }

   @Override
   public String toString() {
      return getClass().getSimpleName() + ": " + signature;
   }

   protected abstract Object invokeSafe(Object... args) throws AdaptException, InvocationTargetException,
         IllegalArgumentException, IllegalAccessException, InstantiationException;

   protected boolean isPrivate() {
      return true;
   }
}
