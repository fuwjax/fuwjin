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
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.function.ClassFunction;

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
   public Object initialize(final Object root, final Object obj) {
      if(invoker == null) {
         invoker = new ClassFunction(root.getClass(), name);
      }
      Object result = invoker.invokeSafe(root);
      if(!Postage.isSuccess(result)) {
         result = invoker.invokeSafe(root, obj);
      }
      return result;
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
