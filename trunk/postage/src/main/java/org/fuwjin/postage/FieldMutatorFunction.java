package org.fuwjin.postage;

import java.lang.reflect.Field;

public class FieldMutatorFunction extends AbstractFunction implements Function {
   private static final String ARG_COUNT = "Field %s could not process %d args: %s";
   private static final String SET_EXCEPTION = "Field %s on %s could not be set to %s";
   private static final String TYPE = "Field %s could not cast %s to %s";
   private final Field field;
   private final Class<?> firstParam;

   public FieldMutatorFunction(final Field field, final Class<?> firstParam) {
      super(field.getName(), firstParam, field.getType());
      this.field = field;
      this.firstParam = firstParam;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final FieldMutatorFunction o = (FieldMutatorFunction)obj;
         return super.equals(obj) && field.equals(o.field) && firstParam.equals(o.firstParam);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invokeSafe(final Object... args) {
      if(args.length != 2) {
         return failure(ARG_COUNT, this, args.length, args);
      }
      if(!firstParam.isInstance(args[0])) {
         return failure(TYPE, this, args[0], firstParam);
      }
      try {
         access(field).set(args[0], args[1]);
         return null;
      } catch(final Exception e) {
         return failure(e, SET_EXCEPTION, this, args[0], args[1]);
      }
   }

   @Override
   public String toString() {
      return field.toString();
   }
}
