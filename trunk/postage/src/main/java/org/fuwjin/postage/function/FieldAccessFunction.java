package org.fuwjin.postage.function;

import java.lang.reflect.Field;

import org.fuwjin.postage.Function;

public class FieldAccessFunction extends AbstractFunction implements Function {
   private static final String ARG_COUNT = "Field %s could not process %d args: %s";
   private static final String GET_EXCEPTION = "Field %s on %s could not be retrieved";
   private static final String TYPE = "Field %s could not cast %s to %s";
   private final Field field;
   private final Class<?> firstParam;

   public FieldAccessFunction(final Field field, final Class<?> firstParam) {
      super(field.getName(), firstParam);
      this.field = field;
      this.firstParam = firstParam;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final FieldAccessFunction o = (FieldAccessFunction)obj;
         return super.equals(obj) && field.equals(o.field) && firstParam.equals(o.firstParam);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invokeSafe(final Object... args) {
      if(args.length != 1) {
         return failure(ARG_COUNT, this, args.length, args);
      }
      if(!firstParam.isInstance(args[0])) {
         return failure(TYPE, this, args[0], firstParam);
      }
      try {
         return access(field).get(args[0]);
      } catch(final Exception e) {
         return failure(e, GET_EXCEPTION, this, args[0]);
      }
   }

   @Override
   public String toString() {
      return field.toString();
   }
}
