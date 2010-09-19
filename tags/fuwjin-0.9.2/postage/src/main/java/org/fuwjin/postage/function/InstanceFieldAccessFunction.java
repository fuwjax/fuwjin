package org.fuwjin.postage.function;

import java.lang.reflect.Field;

import org.fuwjin.postage.Function;

public class InstanceFieldAccessFunction extends AbstractFunction implements Function {
   private final Field field;
   private final Object target;

   public InstanceFieldAccessFunction(final Field field, final Object target) {
      super(field.getName(), field.getType(), false);
      this.field = field;
      this.target = target;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceFieldAccessFunction o = (InstanceFieldAccessFunction)obj;
         return super.equals(obj) && field.equals(o.field) && target.equals(o.target);
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
      return access(field).get(target);
   }
}
