package org.fuwjin.postage.function;

import static org.fuwjin.postage.type.ObjectUtils.access;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

public class InstanceFieldMutatorFunction implements FunctionTarget {
   private final Field field;
   private final Object target;

   public InstanceFieldMutatorFunction(final Field field, final Object target) {
      this.field = field;
      this.target = target;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceFieldMutatorFunction o = (InstanceFieldMutatorFunction)obj;
         return super.equals(obj) && field.equals(o.field) && target.equals(o.target);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      access(field).set(target, args[0]);
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
