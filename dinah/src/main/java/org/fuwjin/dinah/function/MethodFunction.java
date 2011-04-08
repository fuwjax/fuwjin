package org.fuwjin.dinah.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.fuwjin.dinah.Function;
import org.fuwjin.util.ArrayUtils;

public class MethodFunction extends ReflectiveFunction implements Function {
   private final Method method;

   public MethodFunction(final String typeName, final Method method, final Type type) {
      super(typeName + '.' + method.getName(), ArrayUtils.prepend(type, method.getParameterTypes()));
      this.method = method;
   }

   @Override
   protected Object invokeImpl(final Object[] args) throws InvocationTargetException, Exception {
      final Object obj = args[0];
      final Object[] realArgs = new Object[args.length - 1];
      System.arraycopy(args, 1, realArgs, 0, args.length - 1);
      return method.invoke(obj, realArgs);
   }

   @Override
   protected Member member() {
      return method;
   }
}
