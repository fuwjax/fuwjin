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
 * Creates a new instance from a message.
 */
public class FactoryTask implements InitializerTask {
   private String name;
   private Function invoker;

   /**
    * Creates a new instance.
    */
   FactoryTask() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param name the name of the message to dispatch
    */
   public FactoryTask(final String name) {
      this.name = name;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final FactoryTask o = (FactoryTask)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), name);
   }

   @Override
   public PogoContext initialize(final String name, final PogoContext input) {
      final Object result = invoker.invokeSafe(input.get());
      return input.newChild(name, result, input.postageException(result));
   }

   @Override
   public void setType(final ReflectionType type) {
      invoker = type.getInvoker(name);
   }

   @Override
   public String toString() {
      return name;
   }
}
