package org.fuwjin.postage.function;

import static org.fuwjin.postage.type.ObjectUtils.access;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

public class ConstructorFunction implements FunctionTarget {
   public static FunctionTarget constructor(final Constructor<?> c) {
      final FunctionTarget target = new ConstructorFunction(c);
      if(c.isVarArgs()) {
         return new VarArgsTarget(target);
      }
      return target;
   }

   private final Constructor<?> c;

   private ConstructorFunction(final Constructor<?> c) {
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
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      return access(c).newInstance(args);
   }

   @Override
   public Type parameterType(final int index) {
      if(index >= requiredArguments()) {
         return null;
      }
      return c.getParameterTypes()[index];
   }

   @Override
   public int requiredArguments() {
      return c.getParameterTypes().length;
   }

   @Override
   public Type returnType() {
      return c.getDeclaringClass();
   }

   @Override
   public String toString() {
      return c.toString();
   }
}
