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
package org.fuwjin.dinah.provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.function.ConstantFunction;
import org.fuwjin.dinah.function.ConstructorFunction;
import org.fuwjin.dinah.function.FieldAccessFunction;
import org.fuwjin.dinah.function.FieldMutatorFunction;
import org.fuwjin.dinah.function.InstanceOfFunction;
import org.fuwjin.dinah.function.IsAssignableFromFunction;
import org.fuwjin.dinah.function.MethodFunction;
import org.fuwjin.dinah.function.StaticFieldAccessFunction;
import org.fuwjin.dinah.function.StaticFieldMutatorFunction;
import org.fuwjin.dinah.function.StaticMethodFunction;
import org.fuwjin.util.TypeUtils;

/**
 * Provider which supplies functions abstracting the reflection classes such as
 * Constructor, Method, and Field, in addition to some virtual methods such as
 * primitive and array access and the instanceof keyword.
 */
public class ReflectiveFunctionProvider extends AbstractFunctionProvider {
   /**
    * Creates a new instance.
    * @param adapter the type converter
    */
   public ReflectiveFunctionProvider(final Adapter adapter) {
      super(adapter);
   }

   @Override
   public void addFunctions(final Type type) {
      add(new ConstantFunction(this, type, "class", type));
      add(new InstanceOfFunction(this, type));
      add(new IsAssignableFromFunction(this, type));
      addConstructors(type);
      addMethods(type, type, true);
      addFields(type, type, true);
      addEnums(type);
   }

   private void addConstructors(final Type type) {
      for(final Constructor<?> constructor: TypeUtils.getDeclaredConstructors(type)) {
         if(access(constructor)) {
            add(new ConstructorFunction(this, type, constructor));
         }
      }
   }

   private void addEnums(final Type type) {
      if(TypeUtils.isEnum(type)) {
         for(final Enum<?> e: TypeUtils.getEnumConstants(type)) {
            add(new ConstantFunction(this, type, e.name(), e));
         }
      }
   }

   private void addField(final Type type, final Field field, final boolean allowStatic) {
      if(Modifier.isStatic(field.getModifiers())) {
         if(allowStatic) {
            add(new StaticFieldAccessFunction(this, type, field));
            add(new StaticFieldMutatorFunction(this, type, field));
         }
      } else {
         add(new FieldAccessFunction(this, type, field));
         add(new FieldMutatorFunction(this, type, field));
      }
   }

   private void addFields(final Type type, final Type reflectType, final boolean allowStatic) {
      if(reflectType != null) {
         for(final Field field: TypeUtils.getDeclaredFields(reflectType)) {
            if(access(field)) {
               addField(type, field, allowStatic);
            }
         }
         addFields(type, TypeUtils.getSupertype(reflectType), false);
      }
   }

   private void addMethod(final Type type, final Method method, final boolean allowStatic) {
      if(Modifier.isStatic(method.getModifiers())) {
         if(allowStatic) {
            add(new StaticMethodFunction(this, type, method));
         } else {
            return;
         }
      } else {
         add(new MethodFunction(this, type, method));
      }
   }

   private void addMethods(final Type type, final Type reflectType, final boolean allowStatic) {
      if(reflectType != null) {
         for(final Method method: TypeUtils.getDeclaredMethods(reflectType)) {
            if(access(method)) {
               addMethod(type, method, allowStatic);
            }
         }
         addMoreMethods(type, reflectType);
      }
   }

   private void addMoreMethods(final Type type, final Type reflectType) {
      if(TypeUtils.isInterface(reflectType)) {
         for(final Type iface: TypeUtils.getInterfaces(reflectType)) {
            addMethods(type, iface, false);
         }
      } else {
         addMethods(type, TypeUtils.getSupertype(reflectType), false);
      }
   }
}
