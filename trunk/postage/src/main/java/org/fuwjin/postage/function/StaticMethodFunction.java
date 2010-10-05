package org.fuwjin.postage.function;

import static org.fuwjin.postage.type.ObjectUtils.access;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

public class StaticMethodFunction implements FunctionTarget {
   public static FunctionTarget method(final Method method) {
      final FunctionTarget target = new StaticMethodFunction(method);
      if(method.isVarArgs()) {
         return new VarArgsTarget(target);
      }
      return target;
   }

   private final Method method;

   private StaticMethodFunction(final Method method) {
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
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      return access(method).invoke(null, args);
   }

   @Override
   public Type parameterType(final int index) {
      if(index < requiredArguments()) {
         return method.getParameterTypes()[index];
      }
      return null;
   }

   @Override
   public int requiredArguments() {
      return method.getParameterTypes().length;
   }

   @Override
   public Type returnType() {
      return method.getReturnType();
   }

   @Override
   public String toString() {
      return method.toString();
   }
}
