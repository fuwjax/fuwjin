package org.fuwjin.postage.function;

import java.lang.reflect.Constructor;


public class ConstructorFunction extends AbstractFunction {
   private static final String ARG_COUNT = "Could not create %s with %d args: %s";
   private static final String EXCEPTION = "Could not create %s with args: %s";
   private final Constructor<?> c;

   public ConstructorFunction(final Constructor<?> c) {
      super("new", c.getParameterTypes());
      this.c = c;
   }

   private Object doCreate(final Object[] args) {
      try {
         return access(c).newInstance(args);
      } catch(final Exception e) {
         return failure(e, EXCEPTION, this, args);
      }
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ConstructorFunction o = (ConstructorFunction)obj;
         return super.equals(obj) && c.equals(o.c);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invokeSafe(final Object... args) {
      if(c.getParameterTypes().length == args.length) {
         return doCreate(args);
      }
      return failure(ARG_COUNT, this, args.length, args);
   }

   @Override
   public String toString() {
      return c.toString();
   }
}
