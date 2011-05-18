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
package org.fuwjin.dinah;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.fuwjin.dinah.adapter.StandardAdapter;
import org.fuwjin.dinah.function.AbstractFunction;
import org.fuwjin.dinah.function.ConstructorFunction;
import org.fuwjin.dinah.function.FieldAccessFunction;
import org.fuwjin.dinah.function.FieldMutatorFunction;
import org.fuwjin.dinah.function.FixedArgsFunction;
import org.fuwjin.dinah.function.InstanceOfFunction;
import org.fuwjin.dinah.function.MethodFunction;
import org.fuwjin.dinah.function.StaticFieldAccessFunction;
import org.fuwjin.dinah.function.StaticFieldMutatorFunction;
import org.fuwjin.dinah.function.StaticMethodFunction;
import org.fuwjin.dinah.function.VarArgsFunction;
import org.fuwjin.util.TypeUtils;

/**
 * Provider which supplies functions abstracting the reflection classes such as
 * Constructor, Method, and Field, in addition to some virtual methods such as
 * primitive and array access and the instanceof keyword.
 */
public class ReflectiveFunctionProvider extends AbstractFunctionProvider {
   private final Adapter adapter;

   public ReflectiveFunctionProvider() {
      this(new StandardAdapter());
   }

   public ReflectiveFunctionProvider(final Adapter adapter) {
      this.adapter = adapter;
   }

   @Override
   public Map<String, AbstractFunction> getFunctions(final String typeName) {
      final Map<String, AbstractFunction> functions = new HashMap<String, AbstractFunction>();
      try {
         final Type type = TypeUtils.forName(typeName);
         addType(functions, typeName, type);
      } catch(final ClassNotFoundException e) {
         // continue, return empty functions map
      }
      return functions;
   }

   private void addConstructor(final Map<String, AbstractFunction> functions, final String typeName,
         final Constructor<?> constructor) {
      if(constructor.isVarArgs()) {
         add(functions, new VarArgsFunction(adapter, new ConstructorFunction(adapter, typeName, constructor)));
      } else {
         add(functions, new ConstructorFunction(adapter, typeName, constructor));
      }
   }

   private void addField(final Map<String, AbstractFunction> functions, final String typeName, final Type type,
         final Field field, final boolean allowStatic) {
      if(Modifier.isStatic(field.getModifiers())) {
         if(allowStatic) {
            add(functions, new StaticFieldAccessFunction(adapter, typeName, field));
            add(functions, new StaticFieldMutatorFunction(adapter, typeName, field));
         }
      } else {
         add(functions, new FieldAccessFunction(adapter, typeName, field, type));
         add(functions, new FieldMutatorFunction(adapter, typeName, field, type));
      }
   }

   private void addFields(final Map<String, AbstractFunction> functions, final String typeName, final Type type,
         final Type reflectType, final boolean allowStatic) {
      if(reflectType != null) {
         for(final Field field: TypeUtils.getDeclaredFields(reflectType)) {
            if(access(field)) {
               addField(functions, typeName, type, field, allowStatic);
            }
         }
         addFields(functions, typeName, type, TypeUtils.getSupertype(reflectType), false);
      }
   }

   private void addMethod(final Map<String, AbstractFunction> functions, final String typeName, final Type type,
         final Method method, final boolean allowStatic) {
      FixedArgsFunction<?> func;
      if(Modifier.isStatic(method.getModifiers())) {
         if(allowStatic) {
            func = new StaticMethodFunction(adapter, typeName, method);
         } else {
            return;
         }
      } else {
         func = new MethodFunction(adapter, typeName, method, type);
      }
      if(method.isVarArgs()) {
         add(functions, new VarArgsFunction(adapter, func));
      } else {
         add(functions, func);
      }
   }

   private void addMethods(final Map<String, AbstractFunction> functions, final String typeName, final Type type,
         final Type reflectType, final boolean allowStatic) {
      if(reflectType != null) {
         for(final Method method: TypeUtils.getDeclaredMethods(reflectType)) {
            if(access(method)) {
               addMethod(functions, typeName, type, method, allowStatic);
            }
         }
         addMoreMethods(functions, typeName, type, reflectType);
      }
   }

   private void addMoreMethods(final Map<String, AbstractFunction> functions, final String typeName, final Type type,
         final Type reflectType) {
      if(TypeUtils.isInterface(reflectType)) {
         for(final Type iface: TypeUtils.getInterfaces(reflectType)) {
            addMethods(functions, typeName, type, iface, false);
         }
      } else {
         addMethods(functions, typeName, type, TypeUtils.getSupertype(reflectType), false);
      }
   }

   private void addType(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      add(functions, new InstanceOfFunction(adapter, typeName, type));
      for(final Constructor<?> constructor: TypeUtils.getDeclaredConstructors(type)) {
         if(access(constructor)) {
            addConstructor(functions, typeName, constructor);
         }
      }
      addMethods(functions, typeName, type, type, true);
      addFields(functions, typeName, type, type, true);
   }
}
