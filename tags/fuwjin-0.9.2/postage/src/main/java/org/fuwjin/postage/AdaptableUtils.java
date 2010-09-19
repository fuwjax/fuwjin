package org.fuwjin.postage;

import java.util.HashMap;
import java.util.Map;

public class AdaptableUtils {
   private static final Map<Class<?>, Class<?>> wrappers = new HashMap<Class<?>, Class<?>>();
   static {
      wrappers.put(boolean.class, Boolean.class);
      wrappers.put(byte.class, Byte.class);
      wrappers.put(char.class, Character.class);
      wrappers.put(double.class, Double.class);
      wrappers.put(float.class, Float.class);
      wrappers.put(int.class, Integer.class);
      wrappers.put(long.class, Long.class);
      wrappers.put(short.class, Short.class);
   }

   public static Object adapt(final Class<?> type, final Object value) {
      if(value == null) {
         return null;
      }
      if(value.getClass().equals(type)) {
         return value;
      }
      if(value instanceof Adaptable) {
         return ((Adaptable)value).as(type);
      }
      if(isAssignable(type, value)) {
         return value;
      }
      return fail(value, type);
   }

   public static Failure fail(final Object value, final Class<?> type) {
      return new Failure("Cannot adapt %s to %s", value == null ? null : value.getClass(), type);
   }

   public static TypeHandler getHandler(final Class<?> type) {
      return StandardHandler.getHandler(type);
   }

   private static boolean isAssignable(final Class<?> type, final Object value) {
      if(value == null) {
         return void.class.equals(type) || !type.isPrimitive();
      }
      if(type.isAssignableFrom(value.getClass())) {
         return true;
      }
      return type.isPrimitive() && isWrapper(type, value.getClass());
   }

   private static boolean isWrapper(final Class<?> primitive, final Class<?> wrapper) {
      return getHandler(primitive).isWrapper(wrapper);
   }
}
