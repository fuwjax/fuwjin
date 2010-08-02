package org.fuwjin.postage;

import java.lang.reflect.Field;

public class StaticFieldMutatorFunction extends AbstractFunction implements Function {
   private static final String ARG_COUNT = "Field %s could not process %d args: %s";
   private static final String SET_EXCEPTION = "Static field %s could not be set to %s";
   private final Field field;

   public StaticFieldMutatorFunction(final Field field) {
      super(field.getName(), field.getType());
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
   public Object invokeSafe(final Object... args) {
      if(args.length != 1) {
         return failure(ARG_COUNT, this, args.length, args);
      }
      final Object value = args[0];
      try {
         access(field).set(null, value);
         return null;
      } catch(final Exception e) {
         return failure(e, SET_EXCEPTION, this, value);
      }
   }

   @Override
   public String toString() {
      return field.toString();
   }
}
