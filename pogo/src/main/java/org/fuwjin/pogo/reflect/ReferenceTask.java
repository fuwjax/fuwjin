/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.reflect;

import static org.fuwjin.pogo.reflect.DefaultResultTask.MATCH;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

/**
 * Creates a new context that expects to be filled with the matched parse.
 */
public class ReferenceTask implements InitializerTask {
   private ReflectionType type;

   @Override
   public boolean equals(final Object obj) {
      try {
         final ReferenceTask o = (ReferenceTask)obj;
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
   public Object initialize(final Object root, final Object obj) {
      return type.isInstance(obj) ? obj : MATCH;
   }

   @Override
   public void setType(final ReflectionType type) {
      this.type = type;
   }
}
