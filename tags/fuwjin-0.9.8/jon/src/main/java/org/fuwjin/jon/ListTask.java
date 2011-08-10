package org.fuwjin.jon;


public class ListTask implements Task {
   private final Container list;
   private final int index;

   public ListTask(final Container list, final int index) {
      this.list = list;
      this.index = index;
   }

   @Override
   public void resolve(final Object value) throws Exception {
      list.set(index, value);
   }
}
