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

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.reflect.invoke.Invoker;

/**
 * A reflection type that produces empty dispatches.
 */
public class ObjectType implements ReflectionType {
   private static final String OBJECT = "Object"; //$NON-NLS-1$

   @Override
   public boolean equals(final Object obj) {
      try {
         final ObjectType o = (ObjectType)obj;
         return eq(getClass(), o.getClass());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public Invoker getInvoker(final String name) {
      return new Invoker(Object.class, name);
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public boolean isInstance(final Object input) {
      return input != null;
   }

   @Override
   public String toString() {
      return OBJECT;
   }
}
