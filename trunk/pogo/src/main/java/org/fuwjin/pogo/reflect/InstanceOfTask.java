/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.reflect;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;
import org.fuwjin.postage.Function;

/**
 * Creates a child context if the parent context is of the expected type.
 */
public class InstanceOfTask implements InitializerTask {
   private static final String INSTANCEOF = "instanceof"; //$NON-NLS-1$
   private Function function;

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceOfTask o = (InstanceOfTask)obj;
         return eq(getClass(), o.getClass()) && eq(function, o.function);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), function);
   }

   @Override
   public PogoContext initialize(final String name, final PogoContext input) {
      final Object obj = input.get();
      final Object result = function.invokeSafe(obj);
      return input.newChild(name, obj, input.postageException(result));
   }

   @Override
   public void setType(final ReflectionType type) {
      function = type.getInvoker(INSTANCEOF);
   }

   @Override
   public String toString() {
      return INSTANCEOF;
   }
}
