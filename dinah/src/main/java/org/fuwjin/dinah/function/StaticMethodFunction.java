package org.fuwjin.dinah.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.fuwjin.dinah.Function;

public class StaticMethodFunction extends ReflectiveFunction implements Function {
   private final Method method;

   public StaticMethodFunction(final String typeName, final Method method) {
      super(typeName + '.' + method.getName(), method.getParameterTypes());
      this.method = method;
   }

   @Override
   protected Object invokeImpl(final Object[] args) throws InvocationTargetException, Exception {
      return method.invoke(null, args);
   }

   @Override
   protected Member member() {
      return method;
   }
}
