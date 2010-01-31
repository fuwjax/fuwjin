package org.fuwjin.jon.builder;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ReferenceStorage;

public class StringInstanceBuilder extends LiteralBuilder {
   private String value;

   public StringInstanceBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return null;
   }

   @Override
   public void set(final String value) {
      this.value = value;
   }

   @Override
   public String toObjectImpl() {
      return value;
   }
}
