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

import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;

/**
 * Creates a new instance from a default constructor.
 */
public class ConstructorTask implements InitializerTask {
   private static final String NEW = "new"; //$NON-NLS-1$
   private Function invoker;

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
   public Object initialize(final Object root, final Object input) {
      Object result = invoker.invokeSafe();
      if(!Postage.isSuccess(result)) {
         result = invoker.invokeSafe(input);
      }
      return result;
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
