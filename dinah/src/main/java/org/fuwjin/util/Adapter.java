package org.fuwjin.util;

import java.lang.reflect.Type;

public class Adapter {
   public static interface AlertTarget {
      void alert(String pattern, Object... args);

      boolean isSuccess();
   }

   private static final Object UNSET = new Object();

   public static Object adapt(final Object value, final Type type, final AlertTarget target) {
      Object val = value;
      if(!isSet(val)) {
         target.alert("Could not map unset value to %s", type);
      } else if(type != null && !TypeUtils.isInstance(type, val)) {
         if(val instanceof Number) {
            if(type == byte.class) {
               val = ((Number)val).byteValue();
            } else if(type == short.class) {
               val = ((Number)val).shortValue();
            } else if(type == int.class) {
               val = ((Number)val).intValue();
            } else if(type == long.class) {
               val = ((Number)val).longValue();
            } else if(type == float.class) {
               val = ((Number)val).floatValue();
            } else if(type == double.class) {
               val = ((Number)val).doubleValue();
            } else {
               target.alert("Could not map number to %s", type);
            }
         } else {
            target.alert("Could not map value to %s", type);
         }
      }
      return val;
   }

   public static boolean isAdaptable(final Object value, final Type type) {
      return isSet(value) && TypeUtils.isInstance(type, value);
   }

   public static boolean isSet(final Object value) {
      return value != UNSET;
   }

   public static Object unset() {
      return UNSET;
   }
}
