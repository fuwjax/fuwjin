package org.fuwjin.postage.function;

import java.lang.reflect.Method;

public class InstanceMethodFunction extends AbstractFunction {
   private final Method method;
   private final Object target;

   public InstanceMethodFunction(final Method method, final Object target) {
      super(method.getName(), method.getReturnType(), method.isVarArgs(), method.getParameterTypes());
      this.method = method;
      this.target = target;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceMethodFunction o = (InstanceMethodFunction)obj;
         return super.equals(obj) && method.equals(o.method)
               && (target == null ? o.target == null : target.equals(o.target));
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
      return access(method).invoke(target, args);
   }
}
