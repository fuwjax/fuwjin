/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.reflect;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.reflect.invoke.Invoker;

/**
 * Creates a new instance from a default constructor.
 */
public class ConstructorTask implements InitializerTask {
   private static final String DEFAULT_CONSTRUCTOR_FAILED = "default constructor failed"; //$NON-NLS-1$
   private static final String NEW = "new"; //$NON-NLS-1$
   private Invoker invoker;

   @Override
   public boolean equals(final Object obj) {
      try {
         final ConstructorTask o = (ConstructorTask)obj;
         return eq(getClass(), o.getClass()) && eq(invoker, o.invoker);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), invoker);
   }

   @Override
   public PogoContext initialize(final PogoContext input) {
      final Object result = invoker.invoke(input.get());
      return input.newChild(result, isSuccess(result), DEFAULT_CONSTRUCTOR_FAILED);
   }

   @Override
   public void setType(final ReflectionType type) {
      invoker = type.getInvoker(NEW);
   }

   @Override
   public String toString() {
      return NEW;
   }
}
