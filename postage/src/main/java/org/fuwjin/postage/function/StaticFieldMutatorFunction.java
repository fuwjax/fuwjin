package org.fuwjin.postage.function;

import static org.fuwjin.postage.type.ObjectUtils.access;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

public class StaticFieldMutatorFunction implements FunctionTarget {
   private final Field field;

   public StaticFieldMutatorFunction(final Field field) {
      this.field = field;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final StaticFieldMutatorFunction o = (StaticFieldMutatorFunction)obj;
         return super.equals(obj) && field.equals(o.field);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      access(field).set(null, args[0]);
      return null;
   }

   @Override
   public Type parameterType(final int index) {
      if(index == 0) {
         return field.getType();
      }
      return null;
   }

   @Override
   public int requiredArguments() {
      return 1;
   }

   @Override
   public Type returnType() {
      return void.class;
   }

   @Override
   public String toString() {
      return field.toString();
   }
}
