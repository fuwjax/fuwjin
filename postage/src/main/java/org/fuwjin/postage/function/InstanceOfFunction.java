package org.fuwjin.postage.function;


public class InstanceOfFunction extends AbstractFunction {
   private static final String ARG_COUNT = "Could not cast to %s with %d args: %s";
   private static final String EXCEPTION = "Could not cast to %s from %s";
   private final Class<?> type;

   public InstanceOfFunction(final Class<?> type) {
      super("instanceof", type);
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
   public Object invokeSafe(final Object... args) {
      if(args.length != 1) {
         return failure(ARG_COUNT, type, args.length, args);
      }
      if(type.isInstance(args[0])) {
         return args[0];
      }
      return failure(EXCEPTION, type, args[0]);
   }

   @Override
   public String toString() {
      return type.getCanonicalName();
   }
}
