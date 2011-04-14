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

import java.lang.reflect.Array;

/**
 * Utility class for custom array manipulation.
 */
public final class ArrayUtils {
   /**
    * Safely copies the type of any array to the desired length.
    * @param <T> the element type
    * @param array the array to replicate
    * @param length the new array length
    * @return the new array
    */
   public static <T>T[] dup(final T[] array, final int length) {
      return (T[])Array.newInstance(array.getClass().getComponentType(), length);
   }

   /**
    * Prepends the value to the array.
    * @param array the array
    * @param value the prepended value
    * @param <T> the element type
    * @return the new array with the prepended value
    */
   public static <T>T[] push(final T[] array, final T value) {
      final T[] newArray = dup(array, Array.getLength(array) + 1);
      System.arraycopy(array, 0, newArray, 1, Array.getLength(array));
      Array.set(newArray, 0, value);
      return newArray;
   }

   /**
    * Slices the array from start to end. The valid ranges for start and end are
    * from -length to length where length is the length of the array. In
    * addition, start%length <= end%length must be true.
    * @param <T> the element type
    * @param array the array to slice
    * @param start the starting index (inclusive), may be negative to indicate
    *        position from the end.
    * @param end the ending index (exclusive), may be negative to indicate
    *        position from the end.
    * @return the sliced array
    */
   public static <T>T[] slice(final T[] array, final int start, final int end) {
      final int len = Array.getLength(array);
      int offset = start;
      if(start < 0) {
         offset = start + len;
      }
      int length = end - offset;
      if(end < 0) {
         length = length + len;
      }
      final T[] newArray = dup(array, length);
      System.arraycopy(array, offset, newArray, 0, length);
      return newArray;
   }

   private ArrayUtils() {
      // utility class
   }
}
