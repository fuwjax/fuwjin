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

public class TypeUtils {
   /**
    * Returns the component type of the arrayType. Returns null if the arrayType
    * is not an array type.
    * @param type the array type
    * @return the component type, or null if not an array type
    */
   public static Type getComponentType(final Type type) {
      return handler(type).getComponentType(type);
   }

   public static Type[] getUpperBounds(final Type type) {
      return handler(type).getUpperBounds(type);
   }

   private static TypeHandler handler(final Type type) {
      if(type == null) {
         return TypeHandler.NULL;
      }
      if(type instanceof Class) {
         return TypeHandler.CLASS;
      }
      if(type instanceof ExtendedType) {
         return TypeHandler.EXTENDED;
      }
      if(type instanceof WildcardType) {
         return TypeHandler.WILDCARD;
      }
      return null;
   }

   /**
    * Returns the variable type that could be assigned both instance types. If
    * either type is assignable to the other, then this method returns the
    * latter. If either type is null or void, this method returns null.
    * Otherwise, this method returns a wildcard type with both lower bounds.
    * @param type1 the first variable type
    * @param type2 the second variable type
    * @return the
    */
   public static Type intersect(final Type type1, final Type type2) {
      if(type1 == null || type2 == null) {
         return null;
      }
      if(isAssignableTo(type1, type2)) {
         return type2;
      }
      if(isAssignableTo(type2, type1)) {
         return type1;
      }
      return null;
   }

   /**
    * Returns true if the instance can be assigned to the variable type.
    * @param type the variable type
    * @param instance the instance
    * @return true if the instance can be assigned to the type
    */
   public static boolean isAssignable(final Type type, final Object instance) {
      if(instance == null) {
         return !handler(type).isPrimitive(type);
      }
      return handler(type).isInstance(type, instance);
   }

   /**
    * Returns true if an instance of type test would be assignable to a variable
    * of type type.
    * @param variableType the expected super type
    * @param instanceType the instance type
    * @return true if the instance type can be assigned to the variable type
    */
   public static boolean isAssignableFrom(final Type variableType, final Class<?> instanceType) {
      return handler(variableType).isAssignableFrom(variableType, instanceType);
   }

   /**
    * Returns true if an instance of the instanceType could be assigned to the
    * variableType.
    * @param instanceType the instance type
    * @param variableType the variable type
    * @return true if the instance type can be assigned to the variable type
    */
   public static boolean isAssignableTo(final Type instanceType, final Type variableType) {
      return handler(instanceType).isAssignableTo(instanceType, variableType);
   }

   public static boolean isClass(final Type type) {
      return handler(type).isClass(type);
   }

   /**
    * Returns true if the instance can reflect as the type.
    * @param type the reflection type
    * @param instance the instance
    * @return true if the instance function signature set contains the same
    *         signature set of the type
    */
   public static boolean isInstance(final Type type, final Object instance) {
      return handler(type).isInstance(type, instance);
   }

   /**
    * Returns the instance type that could be assigned to both variable types.
    * If either type is assignable to the other, then this method returns the
    * former. If either type is null or void, this method returns null.
    * Otherwise, this method returns a wildcard type with both upper bounds.
    * @param type1 the first variable type
    * @param type2 the second variable type
    * @return the instance type
    */
   public static Type union(final Type type1, final Type type2) {
      if(type1 == null || type2 == null) {
         return null;
      }
      if(isAssignableTo(type1, type2)) {
         return type1;
      }
      if(isAssignableTo(type2, type1)) {
         return type2;
      }
      if(void.class.equals(type1) || void.class.equals(type2)) {
         return null;
      }
      if(isClass(type1)) {
         if(isClass(type2)) {
            return null;
         }
         return ExtendedWildcard.union(type1, type2);
      }
      return ExtendedWildcard.union(type2, type1);
   }
}
