/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.util;

import java.lang.reflect.Type;

public class Adapter {
   public static class AdaptException extends BusinessException {
      public AdaptException(final String pattern, final Object... args) {
         super(pattern, args);
      }
   }

   private static final Object UNSET = new Object();

   public static Object adapt(final Object value, final Type type) throws AdaptException {
      if(!isSet(value)) {
         throw new AdaptException("Could not map unset value to %s", type);
      } else if(value != null && type != null && !TypeUtils.isInstance(type, value)) {
         if(value instanceof Number) {
            if(type == byte.class) {
               return ((Number)value).byteValue();
            } else if(type == short.class) {
               return ((Number)value).shortValue();
            } else if(type == int.class) {
               return ((Number)value).intValue();
            } else if(type == long.class) {
               return ((Number)value).longValue();
            } else if(type == float.class) {
               return ((Number)value).floatValue();
            } else if(type == double.class) {
               return ((Number)value).doubleValue();
            } else {
               throw new AdaptException("Could not map %s to %s", value.getClass(), type);
            }
         } else {
            throw new AdaptException("Could not map %s to %s", value.getClass(), type);
         }
      }
      return value;
   }

   public static Object[] adaptArray(final Object[] array, final Type[] types) throws AdaptException {
      final Object[] result = new Object[array.length];
      for(int i = 0; i < array.length; i++) {
         result[i] = Adapter.adapt(array[i], types[i]);
      }
      return result;
   }

   public static boolean isAdaptable(final Object value, final Type type) {
      return isSet(value) && TypeUtils.isInstance(type, value);
   }

   public static boolean isSet(final Object value) {
      return value != UNSET;
   }

   public static Object unset() {
      return UNSET;
   }
}
