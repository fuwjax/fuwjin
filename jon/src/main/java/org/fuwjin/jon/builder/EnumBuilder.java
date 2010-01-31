package org.fuwjin.jon.builder;

import static java.lang.Enum.valueOf;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.LiteralReference;
import org.fuwjin.jon.ref.ReferenceStorage;

public class EnumBuilder extends LiteralBuilder {
   private Enum<?> value;

   public EnumBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return new LiteralReference(null, cast(storage, obj, cls), obj);
   }

   @Override
   public void set(final String value) {
      this.value = valueOf((Class)type(), value);
   }

   @Override
   public Enum<?> toObjectImpl() {
      return value;
   }
}
