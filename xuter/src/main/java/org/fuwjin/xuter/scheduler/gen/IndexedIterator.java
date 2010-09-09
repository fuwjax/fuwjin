package org.fuwjin.xuter.scheduler.gen;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexedIterator<T> implements Iterator<IndexedValue<T>> {
   private final T[] array;
   private int index;

   public IndexedIterator(final T[] array) {
      this.array = array;
   }

   @Override
   public boolean hasNext() {
      return index < array.length;
   }

   @Override
   public IndexedValue<T> next() {
      if(!hasNext()) {
         throw new NoSuchElementException();
      }
      final int i = index++;
      return new IndexedValue<T>(i, array[i]);
   }

   @Override
   public void remove() {
      throw new UnsupportedOperationException();
   }
}
