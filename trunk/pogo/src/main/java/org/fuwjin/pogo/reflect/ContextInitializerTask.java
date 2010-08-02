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
import org.fuwjin.postage.ClassFunction;
import org.fuwjin.postage.Function;

/**
 * Creates a child context if the parent context is of the expected type.
 */
public class ContextInitializerTask implements InitializerTask {
   private static final String CONTEXT = "context."; //$NON-NLS-1$
   private String name;
   private Function invoker;

   /**
    * Creates a new instance.
    */
   ContextInitializerTask() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param name the context message to dispatch
    */
   public ContextInitializerTask(final String name) {
      this.name = name;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ContextInitializerTask o = (ContextInitializerTask)obj;
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
      if(invoker == null) {
         invoker = new ClassFunction(input.getClass(), this.name);
      }
      final Object obj = invoker.invokeSafe(input);
      return input.newChild(name, obj, input.postageException(obj));
   }

   @Override
   public void setType(final ReflectionType type) {
      // ignore
   }

   @Override
   public String toString() {
      return CONTEXT + name;
   }
}
