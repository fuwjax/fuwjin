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

/**
 * Appends a child context object to the parent context object through a message
 * dispatch.
 */
public class AppendTask implements ConverterTask {
   private String name;
   private transient Function invoker;

   /**
    * Creates a new instance.
    */
   AppendTask() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param name the name of the message to dispatch
    */
   public AppendTask(final String name) {
      this.name = name;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final AppendTask o = (AppendTask)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public Object finalize(final Object container, final Object result) {
      invoker.invokeSafe(container, result);
      return container;
   }

   @Override
   public int hashCode() {
      return hash(getClass(), name);
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
