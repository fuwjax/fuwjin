package org.fuwjin.xuter.scheduler.gen;

public class IndexedValue<T> {
   private final T type;
   private final int index;

   public IndexedValue(final int index, final T type) {
      this.index = index;
      this.type = type;
   }

   public int getIndex() {
      return index;
   }

   public T getValue() {
      return type;
   }
}
