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

import java.util.Arrays;

/**
 * Utility methods for java.lang.Object methods.
 */
public abstract class ObjectUtils {
   /**
    * Assistant for overwriting Object methods efficiently.
    */
   public interface ObjectHelper {
      /**
       * Returns the identity of the object. The array returned must never be
       * altered.
       * @return the identity
       */
      Object[] identity();
   }

   /**
    * Returns a hash code appropriate for obj.
    * @param obj the object to hash
    * @return the hash code
    */
   public static int hash(final ObjectHelper obj) {
      return Arrays.hashCode(obj.identity());
   }

   /**
    * Returns true if obj and other are equal, false otherwise.
    * @param obj the object
    * @param other the test object
    * @return true if obj equals other, false otherwise
    */
   public static boolean isEqual(final ObjectHelper obj, final Object other) {
      if(obj == other) {
         return true;
      }
      return other != null && obj.getClass().equals(other.getClass())
            && Arrays.equals(obj.identity(), ((ObjectHelper)other).identity());
   }

   /**
    * Returns a String suitable for debugging.
    * @param obj the object to stringify
    * @return the string
    */
   public static String toString(final ObjectHelper obj) {
      return obj.getClass().getCanonicalName() + Arrays.toString(obj.identity());
   }

   private ObjectUtils() {
      // utility class
   }
}
