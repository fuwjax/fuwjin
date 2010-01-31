package org.fuwjin.jon.builder;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.MapReference;
import org.fuwjin.jon.ref.ReferenceStorage;

public class MapBuilder extends EntriesBuilder {
   public MapBuilder() {
      super(HashMap.class);
   }

   public MapBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public EntryBuilder newEntry() {
      return new EntryBuilder() {
         @Override
         public Builder newKey() {
            return null;
         }

         @Override
         public Builder newValue() {
            return null;
         }

         @Override
         public void storeImpl() {
            map().put(key, value);
         }

         private Map<Object, Object> map() {
            return (Map<Object, Object>)target;
         }
      };
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return new MapReference(storage.nextName(), storage, cast(storage, obj, cls), (Map<?, ?>)obj, null, null);
   }
}
