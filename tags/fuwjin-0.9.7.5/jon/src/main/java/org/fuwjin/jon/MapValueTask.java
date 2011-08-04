package org.fuwjin.jon;


public class MapValueTask implements Task {
   private final Container map;
   private final Object key;

   public MapValueTask(final Container map, final Object key) {
      this.map = map;
      this.key = key;
   }

   @Override
   public void resolve(final Object value) throws Exception {
      map.put(key, value);
   }
}
