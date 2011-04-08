package org.fuwjin.dinah.function;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

public class FieldAccessFunction extends ReflectiveFunction {
   private final Field field;

   public FieldAccessFunction(final String typeName, final Field field, final Type type) {
      super(typeName + '.' + field.getName(), type);
      this.field = field;
   }

   @Override
   protected Object invokeImpl(final Object[] args) throws InvocationTargetException, Exception {
      return field.get(args[0]);
   }

   @Override
   protected Member member() {
      return field;
   }
}
