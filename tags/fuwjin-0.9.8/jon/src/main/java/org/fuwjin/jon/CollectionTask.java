package org.fuwjin.jon;


public class CollectionTask implements Task {
   private final Container c;

   public CollectionTask(final Container c) {
      this.c = c;
   }

   @Override
   public void resolve(final Object value) throws Exception {
      c.add(value);
   }
}
