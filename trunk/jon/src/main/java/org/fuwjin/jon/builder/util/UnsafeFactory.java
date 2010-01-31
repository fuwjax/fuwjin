/*
 * This file is part of JON.
 *
 * JON is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * JON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2007 Michael Doberenz
 */
package org.fuwjin.jon.builder.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.fuwjin.pogo.reflect.invoke.InvokeHandler;

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
         return InvokeHandler.FAILURE;
      }
   }

   private UnsafeFactory() {
      // utility class
   }
}
