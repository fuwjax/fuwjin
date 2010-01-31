package org.fuwjin.jon.builder;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.jon.ref.StringReference;

public class CharSequenceBuilder extends LiteralBuilder {
   private Object value;

   public CharSequenceBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return new StringReference(storage.nextName(), cast(storage, obj, cls), obj);
   }

   @Override
   public void set(final String value) {
      this.value = newInstance(value);
   }

   @Override
   public Object toObjectImpl() {
      return value;
   }
}
