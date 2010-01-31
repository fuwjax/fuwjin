package org.fuwjin.jon.ref;

import static org.fuwjin.jon.BuilderRegistry.getBuilder;

import java.util.IdentityHashMap;

public class ReferenceStorage {
   private final IdentityHashMap<Object, BaseReference> map = new IdentityHashMap<Object, BaseReference>();
   private int index;

   public Object get(final Object obj, final Class<?> type) {
      if(obj == null) {
         return null;
      }
      BaseReference ref = map.get(obj);
      if(ref == null) {
         ref = getBuilder(obj.getClass()).newReference(obj, type, this);
         if(ref == null) {
            return obj;
         }
         map.put(obj, ref);
      } else {
         ref.queuedForWriting();
      }
      return ref;
   }

   public String nextName() {
      return Integer.toString(index++);
   }
}
