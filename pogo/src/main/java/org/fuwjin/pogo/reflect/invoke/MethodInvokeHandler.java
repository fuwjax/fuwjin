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
package org.fuwjin.pogo.reflect.invoke;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An InvokeHandler that targets methods.
 */
public class MethodInvokeHandler implements InvokeHandler {
   private final Method method;

   /**
    * Creates a new instance.
    * @param method the target method
    */
   public MethodInvokeHandler(final Method method) {
      method.setAccessible(true);
      this.method = method;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final MethodInvokeHandler o = (MethodInvokeHandler)obj;
         return eq(getClass(), o.getClass()) && eq(method, o.method);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), method);
   }

   @Override
   public Object invoke(final Object target, final Object... args) throws Exception {
      if(args.length == method.getParameterTypes().length) {
         try {
            return method.invoke(target, args);
         } catch(final InvocationTargetException e) {
            if(!(e.getCause() instanceof IllegalArgumentException)) {
               throw (Exception)e.getCause();
            }
         } catch(final IllegalArgumentException e) {
            // continue
         }
      }
      return FAILURE;
   }

   @Override
   public Class<?>[] paramTypes(final int paramCount) {
      if(paramCount == method.getParameterTypes().length) {
         return method.getParameterTypes();
      }
      return null;
   }

   @Override
   public String toString() {
      return method.toString();
   }
}
