package org.fuwjin.postage.function;

public class InstanceOfFunction extends AbstractFunction {
   private final Class<?> type;

   public InstanceOfFunction(final Class<?> type) {
      super("instanceof", boolean.class, false, Object.class);
      this.type = type;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceOfFunction o = (InstanceOfFunction)obj;
         return super.equals(obj) && type.equals(o.type);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public String toString() {
      return type.getCanonicalName();
   }

   @Override
   public Object tryInvoke(final Object... args) {
      return type.isInstance(args[0]);
   }
}
