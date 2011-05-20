package org.fuwjin.dinah.adapter;

import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.util.TypeUtils;

/**
 * A default implementation of Adapter.
 */
public class StandardAdapter implements Adapter {
   /**
    * Returns true if the value is set.
    * @param value the test value
    * @return true if the value is set, false otherwise
    */
   public static boolean isSet(final Object value) {
      return !Adapter.UNSET.equals(value);
   }

   @Override
   public Object adapt(final Object value, final Type type) throws AdaptException {
      if(!isSet(value)) {
         throw new AdaptException("Could not map unset value to %s", type);
      }
      if(value == null || type == null || TypeUtils.isInstance(type, value)) {
         return value;
      }
      if(value instanceof Number) {
         if(type == byte.class) {
            return ((Number)value).byteValue();
         } else if(type == short.class) {
            return ((Number)value).shortValue();
         } else if(type == int.class) {
            return ((Number)value).intValue();
         } else if(type == long.class) {
            return ((Number)value).longValue();
         } else if(type == float.class) {
            return ((Number)value).floatValue();
         } else if(type == double.class) {
            return ((Number)value).doubleValue();
         }
      }
      throw new AdaptException("Could not map %s to %s", value.getClass(), type);
   }
}
