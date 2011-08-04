package org.fuwjin.sample;

public class SampleObject {
   private final int id;
   private final String name;

   public SampleObject() {
      id = -1;
      name = "N/A";
   }

   public SampleObject(final int id, final String name) {
      this.id = id;
      this.name = name;
   }

   public boolean eq(final SampleObject o) {
      return id == o.id && name.equals(o.name);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final SampleObject o = (SampleObject)obj;
         return getClass().equals(o.getClass()) && eq(o);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public String toString() {
      return getClass().getSimpleName() + ": id=" + id + " name=" + name;
   }
}
