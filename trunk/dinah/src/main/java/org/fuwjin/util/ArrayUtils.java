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

public class ArrayUtils {
   public static <T> T[] dup(final T[] array, final int length) {
      return (T[])Array.newInstance(array.getClass().getComponentType(), length);
   }

   public static <T> T[] prepend(final T value, final T[] array) {
      final T[] newArray = dup(array, Array.getLength(array) + 1);
      System.arraycopy(array, 0, newArray, 1, Array.getLength(array));
      Array.set(newArray, 0, value);
      return newArray;
   }

   public static <T> T[] slice(final T[] array, final int start, final int end) {
      final int offset = start < 0 ? start + Array.getLength(array) : start;
      final int length = (end < 0 ? end + Array.getLength(array) : end) - offset;
      if(length < 0 || offset + length > Array.getLength(array)) {
         throw new IllegalArgumentException("Array length " + Array.getLength(array) + " does not allow start: "
               + offset + "(" + start + ")" + " end: " + offset + length + "(" + end + ")");
      }
      final T[] newArray = dup(array, length);
      System.arraycopy(array, offset, newArray, 0, length);
      return newArray;
   }
}
