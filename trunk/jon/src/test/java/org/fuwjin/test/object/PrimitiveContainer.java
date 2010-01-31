package org.fuwjin.test.object;

public class PrimitiveContainer {
   private int index;

   public PrimitiveContainer(final int index) {
      this.index = index;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final PrimitiveContainer o = (PrimitiveContainer)obj;
         return index == o.index;
      } catch(final ClassCastException e) {
         return false;
      }
   }
}
