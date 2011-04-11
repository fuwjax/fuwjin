package org.fuwjin.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TypeUtils {
   private static final Map<String, Class<?>> primitives = new HashMap<String, Class<?>>();
   private static final Map<Class<?>, Class<?>> wrappers = new HashMap<Class<?>, Class<?>>();
   static {
      wrappers.put(int.class, Integer.class);
      wrappers.put(long.class, Long.class);
      wrappers.put(char.class, Character.class);
      wrappers.put(short.class, Short.class);
      wrappers.put(byte.class, Byte.class);
      wrappers.put(boolean.class, Boolean.class);
      wrappers.put(float.class, Float.class);
      wrappers.put(double.class, Double.class);
      wrappers.put(void.class, Void.class);
      for(final Class<?> cls: wrappers.keySet()) {
         primitives.put(cls.getCanonicalName(), cls);
      }
   }

   public static Type forName(final String name) throws ClassNotFoundException {
      try {
         return Class.forName(name);
      } catch(final ClassNotFoundException e) {
         final Class<?> cls = primitives.get(name);
         if(cls == null) {
            throw e;
         }
         return cls;
      }
   }

   public static Type getComponentType(final Type type) {
      return toClass(type).getComponentType();
   }

   public static Constructor<?>[] getDeclaredConstructors(final Type type) {
      return toClass(type).getDeclaredConstructors();
   }

   public static Field[] getDeclaredFields(final Type type) {
      return toClass(type).getDeclaredFields();
   }

   public static Method[] getDeclaredMethods(final Type type) {
      return toClass(type).getDeclaredMethods();
   }

   public static Type[] getInterfaces(final Type type) {
      return toClass(type).getInterfaces();
   }

   public static Type getSupertype(final Type type) {
      return toClass(type).getSuperclass();
   }

   public static boolean isAssignableFrom(final Type type, final Type test) {
      if(type == null || test == null) {
         return true;
      }
      return toClass(type).isAssignableFrom(toClass(test));
   }

   public static boolean isInstance(final Type type, final Object object) {
      final Class<?> cls = toWrapper(type);
      return cls.isInstance(object);
   }

   public static boolean isInterface(final Type type) {
      return toClass(type).isInterface();
   }

   public static Object newArrayInstance(final Type type, final int length) {
      return Array.newInstance(toClass(type), length);
   }

   private static Class<?> toClass(final Type type) {
      return (Class<?>)type;
   }

   public static Class<?> toWrapper(final Type type) {
      Class<?> cls = toClass(type);
      if(cls.isPrimitive()) {
         cls = wrappers.get(cls);
      }
      return cls;
   }
}
