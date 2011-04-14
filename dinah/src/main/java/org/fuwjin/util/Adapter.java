/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.util;

import java.lang.reflect.Type;

/**
 * Utility class for adaptation.
 */
public final class Adapter {
   /**
    * Exception for Adapter methods.
    */
   public static class AdaptException extends BusinessException {
      private static final long serialVersionUID = 1L;

      /**
       * Creates a new instance.
       * @param pattern the message pattern
       * @param args the message arguments
       */
      public AdaptException(final String pattern, final Object... args) {
         super(pattern, args);
      }

      /**
       * Creates a new instance.
       * @param cause the exception cause
       * @param pattern the message pattern
       * @param args the message arguments
       */
      public AdaptException(final Throwable cause, final String pattern, final Object... args) {
         super(cause, pattern, args);
      }
   }

   private static final Object UNSET = new Object();

   /**
    * Adapts the value to the type.
    * @param value the value to adapt
    * @param type the target type
    * @return the adapted object
    * @throws AdaptException if the object cannot be adapted
    */
   public static Object adapt(final Object value, final Type type) throws AdaptException {
      if(!isSet(value)) {
         throw new AdaptException("Could not map unset value to %s", type);
      }
      if(value == null || type == null || TypeUtils.isInstance(type, value)) {
         return value;
      }
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
         }
      }
      throw new AdaptException("Could not map %s to %s", value.getClass(), type);
   }

   /**
    * Adapts the array in place, adapting each element to the corresponding
    * element in types.
    * @param array the values to adapt
    * @param types the target types
    * @throws AdaptException if the array elements cannot be adapted, or the
    *         arrays are not the same size
    */
   public static void adaptArray(final Object[] array, final Type[] types) throws AdaptException {
      if(array.length != types.length) {
         throw new AdaptException("expected %d args not %d", types.length, array.length);
      }
      for(int i = 0; i < array.length; ++i) {
         array[i] = Adapter.adapt(array[i], types[i]);
      }
   }

   /**
    * Returns true if the value is a real value, false if "unset".
    * @param value the test value
    * @return false if value equals unset(), true otherwise
    */
   public static boolean isSet(final Object value) {
      return !UNSET.equals(value);
   }

   /**
    * Returns the unset object.
    * @return the unset object
    */
   public static Object unset() {
      return UNSET;
   }

   private Adapter() {
      // utility class
   }
}
