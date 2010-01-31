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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * An InvokeHandler that targets a constructor.
 */
public class ConstructorInvokeHandler implements InvokeHandler {
   private final Constructor<?> constructor;

   /**
    * Creates a new instance.
    * @param constructor the target constructor
    */
   public ConstructorInvokeHandler(final Constructor<?> constructor) {
      constructor.setAccessible(true);
      this.constructor = constructor;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ConstructorInvokeHandler o = (ConstructorInvokeHandler)obj;
         return eq(getClass(), o.getClass()) && eq(constructor, o.constructor);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), constructor);
   }

   @Override
   public Object invoke(final Object target, final Object... args) throws Exception {
      if(args.length == constructor.getParameterTypes().length) {
         try {
            return constructor.newInstance(args);
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
      if(paramCount == constructor.getParameterTypes().length) {
         return constructor.getParameterTypes();
      }
      return null;
   }

   @Override
   public String toString() {
      return constructor.toString();
   }
}
