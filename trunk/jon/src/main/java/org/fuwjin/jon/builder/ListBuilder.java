package org.fuwjin.jon.builder;

import java.util.List;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ListReference;
import org.fuwjin.jon.ref.ReferenceStorage;

public class ListBuilder extends ElementsBuilder {
   public ListBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public Builder newElement() {
      return null;
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return new ListReference(storage.nextName(), storage, cast(storage, obj, cls), (List<?>)obj, null);
   }

   @Override
   protected void postAdd(final int index, final Object value) {
      list.set(index, value);
   }

   @Override
   protected Object toObjectImpl() {
      return list;
   }
}
