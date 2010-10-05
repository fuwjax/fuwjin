/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon.builder.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.fuwjin.postage.Failure;

/**
 * DIRTY POOL: creates a new object using the Unsafe class.
 * @author michaeldoberenz
 */
public final class UnsafeFactory {
   private static final String UNSAFE_FIELD = "theUnsafe"; //$NON-NLS-1$
   private static final String UNSAFE_CLASS_NAME = "sun.misc.Unsafe";
   private static final String UNSAFE_METHOD_NAME = "allocateInstance";
   private static Method UNSAFE_METHOD;
   private static Object UNSAFE;
   static {
      try {
         final Class<?> unsafeClass = Class.forName(UNSAFE_CLASS_NAME);
         final Field field = unsafeClass.getDeclaredField(UNSAFE_FIELD);
         field.setAccessible(true);
         UNSAFE = field.get(null);
         UNSAFE_METHOD = unsafeClass.getDeclaredMethod(UNSAFE_METHOD_NAME, Class.class);
      } catch(final Exception ex) {
         // Unsafe object is not available
      }
   }

   /**
    * DIRTY POOL: creates using the unsafe object.
    * @param type the class to create
    * @return the newly created instance
    */
   public static Object create(final Class<?> type) {
      try {
         return UNSAFE_METHOD.invoke(UNSAFE, type);
      } catch(final Exception e) {
         return new Failure(true, e, "Failed Unsafe creation");
      }
   }

   private UnsafeFactory() {
      // utility class
   }
}
