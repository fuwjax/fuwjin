package org.fuwjin.jon.builder.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterable implements Iterable<Object> {
   public static Iterable<?> iterable(final Object obj) {
      boolean primElm = obj.getClass().getComponentType().isPrimitive();
      return primElm ? new ArrayIterable(obj) : Arrays.asList((Object[])obj);
   }

   private final Object arr;

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
