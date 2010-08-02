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

/**
 * Migrates the child context object to the container context.
 */
public class ReturnTask implements FinalizerTask {
   private static final String RETURN = "return"; //$NON-NLS-1$

   @Override
   public boolean equals(final Object obj) {
      try {
         final ReturnTask o = (ReturnTask)obj;
         return eq(getClass(), o.getClass());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public void finalize(final PogoContext container, final PogoContext child) {
      final Object obj = child.get();
      container.set(obj, container.postageException(obj));
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public void setType(final ReflectionType type) {
      // ignore
   }

   @Override
   public String toString() {
      return RETURN;
   }
}
