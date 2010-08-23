package org.fuwjin.postage.function;

import java.lang.reflect.Field;

import org.fuwjin.postage.Function;

public class FieldAccessFunction extends AbstractFunction implements Function {
   private final Field field;
   private final Class<?> firstParam;

   public FieldAccessFunction(final Field field, final Class<?> firstParam) {
      super(field.getName(), field.getType(), false, firstParam);
      this.field = field;
      this.firstParam = firstParam;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final FieldAccessFunction o = (FieldAccessFunction)obj;
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
      return access(field).get(args[0]);
   }
}
