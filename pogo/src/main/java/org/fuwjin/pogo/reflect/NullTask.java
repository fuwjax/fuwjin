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

/**
 * A task that does as little as possible.
 */
public class NullTask implements ConverterTask, ConstructTask {
   private static final String NULL = "null"; //$NON-NLS-1$

   @Override
   public boolean equals(final Object obj) {
      try {
         final NullTask o = (NullTask)obj;
         return eq(getClass(), o.getClass());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public Object finalize(final Object container, final Object child) {
      return container;
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public Object initialize(final Object input) {
      return null;
   }

   @Override
   public void setType(final ReflectionType type) {
      // ignore
   }

   @Override
   public String toString() {
      return NULL;
   }
}
