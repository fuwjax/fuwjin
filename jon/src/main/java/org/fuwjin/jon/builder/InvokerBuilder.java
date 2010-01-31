package org.fuwjin.jon.builder;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.pogo.reflect.invoke.Invoker;

public class InvokerBuilder extends LiteralBuilder {
   private Invoker value;

   public InvokerBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public Builder forTypeImpl(final Class<?> newType) {
      return new InvokerBuilder(newType);
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return null;
   }

   @Override
   public void set(final String value) {
      this.value = new Invoker(type(), value);
   }

   @Override
   public Invoker toObjectImpl() {
      return value;
   }
}
