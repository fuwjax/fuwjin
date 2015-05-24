/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.postage.type;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

public enum TypeHandler {
   CLASS {
      public Class<?> cls(final Type type) {
         return (Class<?>)type;
      }

      @Override
      public String getCanonicalName(final Type type) {
         return objCls(type).getCanonicalName();
      }

      @Override
      public Type getComponentType(final Type type) {
         return cls(type).getComponentType();
      }

      @Override
      public Type[] getUpperBounds(final Type type) {
         return new Type[]{type};
      }

      @Override
      public boolean isAssignableFrom(final Type type, final Class<?> test) {
         return objCls(type).isAssignableFrom(objCls(test));
      }

      @Override
      public boolean isAssignableTo(final Type type, final Type test) {
         return TypeUtils.isAssignableFrom(test, cls(type));
      }

      @Override
      public boolean isClass(final Type type) {
         return !cls(type).isInterface();
      }

      @Override
      public boolean isInstance(final Type type, final Object object) {
         if(cls(type).isInstance(object)) {
            return true;
         }
         if(void.class.equals(cls(type))) {
            return object == null;
         }
         return objCls(type).isInstance(object);
      }

      @Override
      public boolean isPrimitive(final Type type) {
         return cls(type).isPrimitive();
      }

      private Class<?> objCls(final Type type) {
         Class<?> cls = cls(type);
         if(cls.isPrimitive()) {
            cls = wrappers.get(cls);
         }
         return cls;
      }
   },
   EXTENDED {
      public ExtendedType cls(final Type type) {
         return (ExtendedType)type;
      }

      @Override
      public String getCanonicalName(final Type type) {
         return cls(type).getCanonicalName();
      }

      @Override
      public Type getComponentType(final Type type) {
         return cls(type).getComponentType();
      }

      @Override
      public Type[] getUpperBounds(final Type type) {
         return cls(type).getUpperBounds();
      }

      @Override
      public boolean isAssignableFrom(final Type type, final Class<?> test) {
         return cls(type).isAssignableFrom(test);
      }

      @Override
      public boolean isAssignableTo(final Type type, final Type test) {
         return cls(type).isAssignableTo(test);
      }

      @Override
      public boolean isClass(final Type type) {
         return cls(type).isClass();
      }

      @Override
      public boolean isInstance(final Type type, final Object object) {
         return cls(type).isInstance(object);
      }
   },
   NULL {
      @Override
      public String getCanonicalName(final Type type) {
         return "null";
      }

      @Override
      public Type getComponentType(final Type type) {
         return null;
      }

      @Override
      public Type[] getUpperBounds(final Type type) {
         return new Type[0];
      }

      @Override
      public boolean isAssignableFrom(final Type type, final Class<?> test) {
         return false;
      }

      @Override
      public boolean isAssignableTo(final Type type, final Type test) {
         return false;
      }

      @Override
      public boolean isClass(final Type type) {
         return false;
      }

      @Override
      public boolean isInstance(final Type type, final Object object) {
         return false;
      }
   },
   WILDCARD {
      public WildcardType cls(final Type type) {
         return (WildcardType)type;
      }

      @Override
      public String getCanonicalName(final Type type) {
         return "?";
      }

      @Override
      public Type getComponentType(final Type type) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Type[] getUpperBounds(final Type type) {
         return cls(type).getUpperBounds();
      }

      @Override
      public boolean isAssignableFrom(final Type variableType, final Class<?> instanceType) {
         for(final Type upper: cls(variableType).getUpperBounds()) {
            if(TypeUtils.isAssignableFrom(upper, instanceType)) {
               return true;
            }
         }
         return false;
      }

      @Override
      public boolean isAssignableTo(final Type instanceType, final Type variableType) {
         for(final Type upper: cls(instanceType).getUpperBounds()) {
            if(TypeUtils.isAssignableTo(upper, variableType)) {
               return true;
            }
         }
         return false;
      }

      @Override
      public boolean isClass(final Type type) {
         final Type[] bounds = cls(type).getUpperBounds();
         if(bounds.length == 0) {
            return false;
         }
         return TypeUtils.isClass(bounds[0]);
      }

      @Override
      public boolean isInstance(final Type type, final Object object) {
         for(final Type upper: cls(type).getUpperBounds()) {
            if(TypeUtils.isInstance(upper, object)) {
               return true;
            }
         }
         return false;
      }
   };
   private static Map<Class<?>, Class<?>> wrappers = new HashMap<Class<?>, Class<?>>();
   static {
      wrappers.put(int.class, Integer.class);
      wrappers.put(long.class, Long.class);
      wrappers.put(char.class, Character.class);
      wrappers.put(short.class, Short.class);
      wrappers.put(byte.class, Byte.class);
      wrappers.put(boolean.class, Boolean.class);
      wrappers.put(float.class, Float.class);
      wrappers.put(double.class, Double.class);
      wrappers.put(void.class, void.class);
   }

   public abstract String getCanonicalName(Type type);

   public abstract Type getComponentType(Type type);

   public abstract Type[] getUpperBounds(Type type);

   public abstract boolean isAssignableFrom(Type variableType, Class<?> instanceType);

   public abstract boolean isAssignableTo(Type instanceType, Type variableType);

   public abstract boolean isClass(Type type);

   public abstract boolean isInstance(Type type, Object object);

   public boolean isPrimitive(final Type type) {
      return false;
   }
}
