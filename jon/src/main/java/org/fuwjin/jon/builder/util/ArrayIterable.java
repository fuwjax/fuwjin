/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.jon.builder.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over an array.
 */
public class ArrayIterable implements Iterable<Object> {
   /**
    * Creates an iterable for the array object.
    * @param obj the array object
    * @return the iterable
    */
   public static Iterable<?> iterable(final Object obj) {
      final boolean primElm = obj.getClass().getComponentType().isPrimitive();
      return primElm ? new ArrayIterable(obj) : Arrays.asList((Object[])obj);
   }

   private final Object arr;

   /**
    * Creates a new instance.
    * @param arr the array
    */
   public ArrayIterable(final Object arr) {
      this.arr = arr;
   }

   @Override
   public Iterator<Object> iterator() {
      return new Iterator<Object>() {
         private int index;

         @Override
         public boolean hasNext() {
            return index < Array.getLength(arr);
         }

         @Override
         public Object next() {
            if(!hasNext()) {
               throw new NoSuchElementException();
            }
            final Object ret = Array.get(arr, index);
            index++;
            return ret;
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }
}
