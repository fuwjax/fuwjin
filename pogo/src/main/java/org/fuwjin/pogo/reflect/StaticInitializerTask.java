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
import org.fuwjin.postage.Postage;

/**
 * Creates a new context from a dispatch method.
 */
public class StaticInitializerTask implements InitializerTask {
   private String name;
   private transient Function invoker;

   /**
    * Creates a new instance.
    */
   StaticInitializerTask() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param name the name of the message to dispatch
    */
   public StaticInitializerTask(final String name) {
      this.name = name;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final StaticInitializerTask o = (StaticInitializerTask)obj;
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
      final Object obj = input.get();
      // if(obj != null && !type.isInstance(obj)) {
      // final Object result = invoker.invokeSafe(obj);
      // return input.newChild(result, Postage.isSuccess(result), ERROR_CODE +
      // type);
      // }
      Object result = invoker.invokeSafe(obj);
      result = Postage.isSuccess(result) ? result : invoker.invokeSafe();
      if(result instanceof Boolean) {
         return input.newChild(name, obj, (Boolean)result ? null : input.failedCheck("boolean result from " + invoker));
      }
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
