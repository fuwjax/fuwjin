package org.fuwjin.dinah.function;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;

public class ConstructorFunction extends ReflectiveFunction {
   private final Constructor<?> constructor;

   public ConstructorFunction(final String typeName, final Constructor<?> constructor) {
      super(typeName + ".new", constructor.getParameterTypes());
      this.constructor = constructor;
   }

   @Override
   protected Object invokeImpl(final Object[] args) throws InvocationTargetException, IllegalArgumentException,
         InstantiationException, IllegalAccessException {
      return constructor.newInstance(args);
   }

   @Override
   protected Member member() {
      return constructor;
   }
}
