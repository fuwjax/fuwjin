/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for generic types.
 */
public final class TypeUtils {
   private static final Map<String, Class<?>> PRIMITIVES = new HashMap<String, Class<?>>();
   private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap<Class<?>, Class<?>>();
   static {
      WRAPPERS.put(int.class, Integer.class);
      WRAPPERS.put(long.class, Long.class);
      WRAPPERS.put(char.class, Character.class);
      WRAPPERS.put(short.class, Short.class);
      WRAPPERS.put(byte.class, Byte.class);
      WRAPPERS.put(boolean.class, Boolean.class);
      WRAPPERS.put(float.class, Float.class);
      WRAPPERS.put(double.class, Double.class);
      WRAPPERS.put(void.class, Void.class);
      for(final Class<?> cls: WRAPPERS.keySet()) {
         PRIMITIVES.put(cls.getCanonicalName(), cls);
      }
   }

   /**
    * Returns the type for the given name. Mimics Class.forName.
    * @param name the type name
    * @return the type
    * @throws ClassNotFoundException if no such type was found
    */
   public static Type forName(final String name) throws ClassNotFoundException {
      try {
         if(name.endsWith("]")) {
            return Class.forName(toArray(name));
         }
         return Class.forName(name);
      } catch(final ClassNotFoundException e) {
         final Class<?> cls = PRIMITIVES.get(name);
         if(cls == null) {
            return inner(name);
         }
         return cls;
      }
   }

   /**
    * Returns the component type of the given type. Mimics
    * Class.getComponentType.
    * @param type the array type
    * @return the component type, or null if type is not an array type
    */
   public static Type getComponentType(final Type type) {
      return toClass(type).getComponentType();
   }

   /**
    * Returns the declared constructors for the given type. Mimics
    * Class.getDeclaredConstructors.
    * @param type the type
    * @return the constructors
    */
   public static Constructor<?>[] getDeclaredConstructors(final Type type) {
      return toClass(type).getDeclaredConstructors();
   }

   /**
    * Returns the declared fields for the given type. Mimics
    * Class.getDeclaredFields.
    * @param type the type
    * @return the fields
    */
   public static Field[] getDeclaredFields(final Type type) {
      return toClass(type).getDeclaredFields();
   }

   /**
    * Returns the declared methods for the given type. Mimics
    * Class.getDeclaredMethods.
    * @param type the type
    * @return the methods
    */
   public static Method[] getDeclaredMethods(final Type type) {
      return toClass(type).getDeclaredMethods();
   }

   /**
    * Returns the interfaces for the given type. Mimics Class.getInterfaces.
    * @param type the type
    * @return the interfaces
    */
   public static Type[] getInterfaces(final Type type) {
      return toClass(type).getInterfaces();
   }

   /**
    * Returns the super type for the given type. Mimics Class.getSupertype.
    * @param type the type
    * @return the super type
    */
   public static Type getSupertype(final Type type) {
      return toClass(type).getSuperclass();
   }

   /**
    * Returns true if type is assignable from test. Mimics
    * Class.isAssignableFrom.
    * @param type the type
    * @param test the assigned type
    * @return true if test can be assigned to type, false otherwise
    */
   public static boolean isAssignableFrom(final Type type, final Type test) {
      if(type == null || test == null) {
         return true;
      }
      return toClass(type).isAssignableFrom(toClass(test));
   }

   /**
    * Returns true if object is an instance of type. Mimics Class.isInstance.
    * @param type the type
    * @param object the test object
    * @return true if object is an instance of type, false otherwise
    */
   public static boolean isInstance(final Type type, final Object object) {
      final Class<?> cls = toWrapper(type);
      return cls.isInstance(object);
   }

   /**
    * Returns true if type is an interface. Mimics Class.isInterface.
    * @param type the test type
    * @return true if type is an interface, false otherwise
    */
   public static boolean isInterface(final Type type) {
      return toClass(type).isInterface();
   }

   /**
    * Creates a new array instance.
    * @param type the component type
    * @param length the length of the new array
    * @return the new array
    */
   public static Object newArrayInstance(final Type type, final int length) {
      return Array.newInstance(toClass(type), length);
   }

   /**
    * Returns a non-primitive equivalent of type.
    * @param type the type
    * @return the wrapper if type is primitive, the type otherwise
    */
   public static Class<?> toWrapper(final Type type) {
      Class<?> cls = toClass(type);
      if(cls.isPrimitive()) {
         cls = WRAPPERS.get(cls);
      }
      return cls;
   }

   private static Type inner(final String name) throws ClassNotFoundException {
      final int index = name.lastIndexOf('.');
      final String value = name.substring(0, index) + '$' + name.substring(index + 1);
      try {
         return Class.forName(value);
      } catch(final ClassNotFoundException e) {
         if(value.indexOf('.') >= 0) {
            return inner(value);
         }
         throw e;
      }
   }

   private static String toArray(final String name) {
      final StringBuilder builder = new StringBuilder();
      int index = name.indexOf('[');
      while(index > -1) {
         builder.append('[');
         index = name.indexOf('[', index + 1);
      }
      return builder.append('L').append(name.substring(0, name.indexOf('['))).append(';').toString();
   }

   /**
    * Returns the class equivalent of the type.
    * @param type the type
    * @return the class equivalent
    */
   private static Class<?> toClass(final Type type) {
      return (Class<?>)type;
   }

   private TypeUtils() {
      // utility class
   }
}
