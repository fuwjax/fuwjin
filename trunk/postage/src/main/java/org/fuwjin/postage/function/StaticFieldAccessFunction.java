package org.fuwjin.postage.function;

import static org.fuwjin.postage.type.ObjectUtils.access;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

public class StaticFieldAccessFunction implements FunctionTarget {
   private final Field field;

   public StaticFieldAccessFunction(final Field field) {
      this.field = field;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final StaticFieldAccessFunction o = (StaticFieldAccessFunction)obj;
         return super.equals(obj) && field.equals(o.field);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      return access(field).get(null);
   }

   @Override
   public Type parameterType(final int index) {
      return null;
   }

   @Override
   public int requiredArguments() {
      return 0;
   }

   @Override
   public Type returnType() {
      return field.getType();
   }

   @Override
   public String toString() {
      return field.toString();
   }
}
