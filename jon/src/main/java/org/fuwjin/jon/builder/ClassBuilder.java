package org.fuwjin.jon.builder;

import org.fuwjin.jon.JonLiteral;
import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.LiteralReference;
import org.fuwjin.jon.ref.ReferenceStorage;

public class ClassBuilder extends LiteralBuilder {
   private Class<?> cls;

   public ClassBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> expCls, final ReferenceStorage storage) {
      return new LiteralReference(storage.nextName(), null, JonLiteral.getName((Class<?>)obj));
   }

   @Override
   public void set(final String value) {
      cls = JonLiteral.forName(value);
   }

   @Override
   public Class<?> toObjectImpl() {
      return cls;
   }
}
