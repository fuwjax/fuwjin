package org.fuwjin.postage.function;

import java.lang.reflect.Field;

import org.fuwjin.postage.Function;

public class InstanceFieldMutatorFunction extends AbstractFunction implements Function {
   private final Field field;
   private final Object target;

   public InstanceFieldMutatorFunction(final Field field, final Object target) {
      super(field.getName(), Void.class, false, field.getType());
      this.field = field;
      this.target = target;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceFieldMutatorFunction o = (InstanceFieldMutatorFunction)obj;
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
      access(field).set(target, args[0]);
      return null;
   }
}
