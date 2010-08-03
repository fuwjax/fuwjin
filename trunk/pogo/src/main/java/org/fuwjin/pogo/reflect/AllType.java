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
import org.fuwjin.postage.category.NullCategory;

/**
 * A reflection type that produces empty dispatches.
 */
public class AllType implements ReflectionType {
   private static final String ALL = "all"; //$NON-NLS-1$

   @Override
   public boolean equals(final Object obj) {
      try {
         final AllType o = (AllType)obj;
         return eq(getClass(), o.getClass());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public Function getInvoker(final String name) {
      return new NullCategory().getFunction(name);
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public boolean isInstance(final Object input) {
      return true;
   }

   @Override
   public String toString() {
      return ALL;
   }
}
