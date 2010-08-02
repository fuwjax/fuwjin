/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.reflect;

import static org.fuwjin.postage.Postage.isSuccess;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;
import org.fuwjin.postage.Function;

/**
 * Migrates the child context to the parent context.
 */
public class ResultTask implements FinalizerTask {
   private String name;
   private transient Function invoker;

   /**
    * Creates a new instance.
    */
   ResultTask() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param name the name of the message to dispatch
    */
   public ResultTask(final String name) {
      this.name = name;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ResultTask o = (ResultTask)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public void finalize(final PogoContext container, final PogoContext child) {
      Object result = invoker.invokeSafe(child.get());
      result = isSuccess(result) ? result : invoker.invokeSafe(child.get(), child.match());
      result = isSuccess(result) ? result : invoker.invokeSafe();
      result = isSuccess(result) ? result : invoker.invokeSafe(child.match());
      container.set(result, container.postageException(result));
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
