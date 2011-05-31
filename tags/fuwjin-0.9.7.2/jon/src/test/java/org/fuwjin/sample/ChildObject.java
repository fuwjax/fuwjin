package org.fuwjin.sample;

import java.util.Arrays;

public class ChildObject extends SampleObject {
   private final int[] values;

   public ChildObject() {
      values = new int[0];
   }

   public ChildObject(final int[] values, final int id, final String name) {
      super(id, name);
      this.values = values;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ChildObject o = (ChildObject)obj;
         return getClass().equals(o.getClass()) && eq(o);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public String toString() {
      return super.toString() + " values=" + Arrays.toString(values);
   }

   private boolean eq(final ChildObject o) {
      return super.eq(o) && Arrays.equals(values, o.values);
   }
}
