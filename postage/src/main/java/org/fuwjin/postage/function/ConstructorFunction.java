package org.fuwjin.postage.function;

import java.lang.reflect.Constructor;

public class ConstructorFunction extends AbstractFunction {
   private final Constructor<?> c;

   public ConstructorFunction(final Constructor<?> c) {
      super("new", c.getDeclaringClass(), c.isVarArgs(), c.getParameterTypes());
      this.c = c;
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
   public String toString() {
      return c.toString();
   }

   @Override
   protected Object tryInvoke(final Object... args) throws Exception {
      return access(c).newInstance(args);
   }
}
