package org.fuwjin.postage.function;

import java.lang.reflect.Field;

import org.fuwjin.postage.Function;

public class FieldMutatorFunction extends AbstractFunction implements Function {
   private final Field field;
   private final Class<?> firstParam;

   public FieldMutatorFunction(final Field field, final Class<?> firstParam) {
      super(field.getName(), Void.class, false, firstParam, field.getType());
      this.field = field;
      this.firstParam = firstParam;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final FieldMutatorFunction o = (FieldMutatorFunction)obj;
         return super.equals(obj) && field.equals(o.field) && firstParam.equals(o.firstParam);
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
      access(field).set(args[0], args[1]);
      return null;
   }
}
