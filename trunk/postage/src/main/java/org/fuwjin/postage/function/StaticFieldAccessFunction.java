package org.fuwjin.postage.function;

import java.lang.reflect.Field;

import org.fuwjin.postage.Function;

public class StaticFieldAccessFunction extends AbstractFunction implements Function {
   private final Field field;

   public StaticFieldAccessFunction(final Field field) {
      super(field.getName(), field.getType(), false);
      this.field = field;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final StaticFieldAccessFunction o = (StaticFieldAccessFunction)obj;
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
      return access(field).get(null);
   }
}
