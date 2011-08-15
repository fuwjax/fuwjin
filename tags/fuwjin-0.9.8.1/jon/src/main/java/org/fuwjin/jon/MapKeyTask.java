package org.fuwjin.jon;


public class MapKeyTask implements Task {
   private final Container map;
   private final Object value;

   public MapKeyTask(final Container map, final Object value) {
      this.map = map;
      this.value = value;
   }

   @Override
   public void resolve(final Object key) throws Exception {
      map.put(key, value);
   }
}
