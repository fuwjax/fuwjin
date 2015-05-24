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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Iterator over a {@link AtomicReferenceArray} that supports an additional
 * convenience compareAndSet method. While the underlying data structure is
 * thread-safe, this iterator should not be shared across threads. 
 * @param <T> the element type
 */
public class AtomicIterator<T> implements Iterator<T>{
   private final AtomicReferenceArray<T> arr;
   private T current;
   private int currentIndex;

   /**
    * Creates a new instance.
    * @param array the array to iterate over
    */
   protected AtomicIterator(final AtomicReferenceArray<T> array){
      this.arr = array;
   }

   @Override
   public boolean hasNext(){
      return currentIndex < arr.length();
   }

   @Override
   public T next(){
      if(!hasNext()){
         throw new NoSuchElementException();
      }
      current = arr.get(currentIndex);
      ++currentIndex;
      return current;
   }

   @Override
   public void remove(){
      replace(null);
   }

   /**
    * Performs a compareAndSet operation on the underlying array at the current
    * position and value.
    * @param value the new value to set at this position in the array
    * @return true if the operation was successful, false otherwise
    */
   public boolean replace(final T value){
      assert currentIndex > 0;
      return arr.compareAndSet(currentIndex - 1, current, value);
   }
}
