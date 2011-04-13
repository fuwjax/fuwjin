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

public class ObjectUtils {
   public interface ObjectHelper {
      Object[] identity();
   }

   public static long hash(final ObjectHelper obj) {
      return Arrays.hashCode(obj.identity());
   }

   public static boolean isEqual(final ObjectHelper obj, final Object other) {
      if(obj == null && other == null) {
         return true;
      }
      if(obj == null || other == null) {
         return false;
      }
      if(obj == other) {
         return true;
      }
      if(obj.getClass().equals(other.getClass())) {
         return Arrays.equals(obj.identity(), ((ObjectHelper)other).identity());
      }
      return false;
   }

   public static String toString(final ObjectHelper obj) {
      return obj.getClass().getCanonicalName() + Arrays.toString(obj.identity());
   }
}
