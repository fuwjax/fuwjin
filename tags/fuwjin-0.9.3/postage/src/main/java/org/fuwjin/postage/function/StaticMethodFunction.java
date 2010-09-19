package org.fuwjin.postage.function;

import java.lang.reflect.Method;

public class StaticMethodFunction extends AbstractFunction {
   private final Method method;

   public StaticMethodFunction(final Method method) {
      super(method.getName(), method.getReturnType(), method.isVarArgs(), method.getParameterTypes());
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
   public String toString() {
      return method.toString();
   }

   @Override
   public Object tryInvoke(final Object... args) throws Exception {
      return access(method).invoke(null, args);
   }
}
