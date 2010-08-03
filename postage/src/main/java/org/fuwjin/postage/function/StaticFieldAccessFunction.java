package org.fuwjin.postage.function;

import java.lang.reflect.Field;

import org.fuwjin.postage.Function;

public class StaticFieldAccessFunction extends AbstractFunction implements Function {
   private static final String ARG_COUNT = "Field %s could not process %d args: %s";
   private static final String GET_EXCEPTION = "Static field %s could not be retrieved";
   private final Field field;

   public StaticFieldAccessFunction(final Field field) {
      super(field.getName());
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
   public Object invokeSafe(final Object... args) {
      if(args.length != 0) {
         return failure(ARG_COUNT, this, args.length, args);
      }
      try {
         return access(field).get(null);
      } catch(final Exception e) {
         return failure(e, GET_EXCEPTION, this);
      }
   }

   @Override
   public String toString() {
      return field.toString();
   }
}
