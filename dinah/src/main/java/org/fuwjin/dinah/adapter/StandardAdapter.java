package org.fuwjin.dinah.adapter;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;
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

   private final ClassLoader loader;

   public StandardAdapter() {
      this(Thread.currentThread().getContextClassLoader());
   }

   public StandardAdapter(final ClassLoader loader) {
      this.loader = loader;
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
      if(value instanceof Collection && TypeUtils.isArray(type)) {
         return toArray((Collection<?>)value, TypeUtils.getComponentType(type));
      }
      if(value instanceof String && TypeUtils.isAssignableFrom(Type.class, type)) {
         try {
            return TypeUtils.forName((String)value, loader);
         } catch(final ClassNotFoundException e) {
            throw new AdaptException(e, "Could not find class named %s", value);
         }
      }
      throw new AdaptException("Could not map %s to %s", value.getClass(), type);
   }

   Object toArray(final Collection<?> collection, final Type type) {
      final Object arr = TypeUtils.newArrayInstance(type, collection.size());
      if(TypeUtils.isPrimitive(type)) {
         int i = 0;
         for(final Object obj: collection) {
            Array.set(arr, i++, obj);
         }
         return arr;
      }
      return collection.toArray((Object[])arr);
   }
}
