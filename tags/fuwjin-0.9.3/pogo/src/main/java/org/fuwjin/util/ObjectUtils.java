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
package org.fuwjin.util;

/**
 * Helper class for standard Object interaction.
 */
public class ObjectUtils {
   /**
    * Returns true if both objects are null or equal, false otherwise.
    * @param o1 the first object
    * @param o2 the second object
    * @return true if both objects are null or equal, false otherwise
    */
   public static boolean eq(final Object o1, final Object o2) {
      return o1 == null ? o2 == null : o1.equals(o2);
   }

   /**
    * Hashes the objects in a standard way.
    * @param objects the set of objects to hash
    * @return the hash result
    */
   public static int hash(final Object... objects) {
      int hash = 47;
      for(final Object o: objects) {
         hash = 31 * hash + (o == null ? 17 : o.hashCode());
      }
      return hash;
   }
}
