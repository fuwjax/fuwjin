package org.fuwjin.postage.function;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class MethodFunction extends AbstractFunction {
   private static <T> T[] prepend(final T first, final T[] array) {
      final T[] trim = (T[])Array.newInstance(array.getClass().getComponentType(), array.length + 1);
      System.arraycopy(array, 0, trim, 1, array.length);
      trim[0] = first;
      return trim;
   }

   private final Method method;
   private final Class<?> firstParam;

   public MethodFunction(final Method method, final Class<?> firstParam) {
      super(method.getName(), method.getReturnType(), method.isVarArgs(), prepend(firstParam,
            method.getParameterTypes()));
      this.method = method;
      this.firstParam = firstParam;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final MethodFunction o = (MethodFunction)obj;
         return super.equals(obj) && method.equals(o.method)
               && (firstParam == null ? o.firstParam == null : firstParam.equals(o.firstParam));
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
      final Object[] trim = new Object[args.length - 1];
      System.arraycopy(args, 1, trim, 0, trim.length);
      final Object object = args[0];
      return access(method).invoke(object, trim);
   }
}
