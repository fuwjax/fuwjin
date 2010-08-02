package org.fuwjin.postage;

public class NullCategory extends AbstractCategory {
   private static final String ARG_COUNT = "Could not test null with %d args: %s";
   private static final String EXCEPTION = "Could not cast %s to null";

   public NullCategory() {
      super("null");
      addFunction(new AbstractFunction(new UnknownSignature("instanceof", 1, 1)) {
         @Override
         public Object invokeSafe(final Object... args) {
            if(args.length != 1) {
               return failure(ARG_COUNT, args.length, args);
            }
            if(args[0] != null) {
               return failure(EXCEPTION, args[0]);
            }
            return null;
         }
      });
   }

   @Override
   protected Function newFunction(final String name) {
      return new ConstantFunction(name, null);
   }
}
