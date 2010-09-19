package org.fuwjin.postage.function;

import java.lang.reflect.Field;

import org.fuwjin.postage.Function;

public class StaticFieldMutatorFunction extends AbstractFunction implements Function {
   private final Field field;

   public StaticFieldMutatorFunction(final Field field) {
      super(field.getName(), Void.class, false, field.getType());
      this.field = field;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final StaticFieldMutatorFunction o = (StaticFieldMutatorFunction)obj;
         return super.equals(obj) && field.equals(o.field);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public String toString() {
      return field.toString();
   }

   @Override
   public Object tryInvoke(final Object... args) throws Exception {
      access(field).set(null, args[0]);
      return null;
   }
}
