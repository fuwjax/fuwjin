package org.fuwjin.postage;

import java.lang.reflect.Method;

public class StaticMethodFunction extends AbstractFunction {
   private static final String ARG_COUNT = "Method %s could not process %d args: %s";
   private static final String EXCEPTION = "Static method %s could not invoke with args: %s";
   private final Method method;

   public StaticMethodFunction(final Method method) {
      super(method.getName(), method.getParameterTypes());
      this.method = method;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final StaticMethodFunction o = (StaticMethodFunction)obj;
         return super.equals(obj) && method.equals(o.method);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invokeSafe(final Object... args) {
      if(method.getParameterTypes().length != args.length) {
         return failure(ARG_COUNT, this, args.length, args);
      }
      try {
         return access(method).invoke(null, args);
      } catch(final Exception e) {
         return failure(e, EXCEPTION, this, args);
      }
   }

   @Override
   public String toString() {
      return method.toString();
   }
}
