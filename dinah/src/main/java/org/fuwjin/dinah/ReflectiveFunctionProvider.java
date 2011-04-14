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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
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
public class ReflectiveFunctionProvider implements FunctionProvider {
   private final Map<String, AbstractFunction> functions = new HashMap<String, AbstractFunction>();

   @Override
   public Function getFunction(final FunctionSignature signature) {
      AbstractFunction function = functions.get(signature.name());
      if(function == null) {
         try {
            final String typeName = signature.category();
            final Type type = TypeUtils.forName(typeName);
            addType(typeName, type);
         } catch(final ClassNotFoundException e) {
            throw new IllegalArgumentException("No category found for " + signature.name(), e);
         }
         function = functions.get(signature.name());
         if(function == null) {
            throw new IllegalArgumentException("No function found for " + signature.name());
         }
      }
      return function.restrict(signature);
   }

   private boolean access(final AccessibleObject obj) {
      if(!obj.isAccessible()) {
         try {
            obj.setAccessible(true);
         } catch(final SecurityException e) {
            return false;
         }
      }
      return true;
   }

   private void add(final AbstractFunction function) {
      final String name = function.name();
      final AbstractFunction func = functions.get(name);
      if(func == null) {
         functions.put(name, function);
      } else {
         functions.put(name, func.join(function));
      }
   }

   private void addConstructor(final String typeName, final Constructor<?> constructor) {
      if(constructor.isVarArgs()) {
         add(new VarArgsFunction(new ConstructorFunction(typeName, constructor)));
      } else {
         add(new ConstructorFunction(typeName, constructor));
      }
   }

   private void addField(final String typeName, final Type type, final Field field, final boolean allowStatic) {
      if(Modifier.isStatic(field.getModifiers())) {
         if(allowStatic) {
            add(new StaticFieldAccessFunction(typeName, field));
            add(new StaticFieldMutatorFunction(typeName, field));
         }
      } else {
         add(new FieldAccessFunction(typeName, field, type));
         add(new FieldMutatorFunction(typeName, field, type));
      }
   }

   private void addFields(final String typeName, final Type type, final Type reflectType, final boolean allowStatic) {
      if(reflectType != null) {
         for(final Field field: TypeUtils.getDeclaredFields(reflectType)) {
            if(access(field)) {
               addField(typeName, type, field, allowStatic);
            }
         }
         addFields(typeName, type, TypeUtils.getSupertype(reflectType), false);
      }
   }

   private void addMethod(final String typeName, final Type type, final Method method, final boolean allowStatic) {
      FixedArgsFunction func;
      if(Modifier.isStatic(method.getModifiers())) {
         if(allowStatic) {
            func = new StaticMethodFunction(typeName, method);
         } else {
            return;
         }
      } else {
         func = new MethodFunction(typeName, method, type);
      }
      if(method.isVarArgs()) {
         add(new VarArgsFunction(func));
      } else {
         add(func);
      }
   }

   private void addMethods(final String typeName, final Type type, final Type reflectType, final boolean allowStatic) {
      if(reflectType != null) {
         for(final Method method: TypeUtils.getDeclaredMethods(reflectType)) {
            if(access(method)) {
               addMethod(typeName, type, method, allowStatic);
            }
         }
         addMoreMethods(typeName, type, reflectType);
      }
   }

   private void addMoreMethods(final String typeName, final Type type, final Type reflectType) {
      if(TypeUtils.isInterface(reflectType)) {
         for(final Type iface: TypeUtils.getInterfaces(reflectType)) {
            addMethods(typeName, type, iface, false);
         }
      } else {
         addMethods(typeName, type, TypeUtils.getSupertype(reflectType), false);
      }
   }

   private void addType(final String typeName, final Type type) {
      add(new InstanceOfFunction(typeName, type));
      for(final Constructor<?> constructor: TypeUtils.getDeclaredConstructors(type)) {
         if(access(constructor)) {
            addConstructor(typeName, constructor);
         }
      }
      addMethods(typeName, type, type, true);
      addFields(typeName, type, type, true);
   }
}
