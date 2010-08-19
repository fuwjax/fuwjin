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

import java.util.Iterator;

import org.fuwjin.postage.function.ClassFunction;
import org.fuwjin.postage.function.CompositeFunction;

/**
 * Creates a new context for the next element of an iterator.
 */
public class NextTask implements ConstructTask {
   private static final String NEXT = "next"; //$NON-NLS-1$
   private CompositeFunction invoker;

   @Override
   public boolean equals(final Object obj) {
      try {
         final NextTask o = (NextTask)obj;
         return eq(getClass(), o.getClass());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public Object initialize(final Object input) {
      return invoker.invokeSafe(input);
   }

   @Override
   public void setType(final ReflectionType type) {
      invoker = new CompositeFunction(NEXT);
      invoker.addFunction(new ClassFunction(Iterator.class, NEXT));
      invoker.addFunction(type.getInvoker(NEXT));
   }

   @Override
   public String toString() {
      return NEXT;
   }
}
