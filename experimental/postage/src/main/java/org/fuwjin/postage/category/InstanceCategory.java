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
package org.fuwjin.postage.category;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.FunctionFactory;
import org.fuwjin.postage.function.ConstantFunction;
import org.fuwjin.postage.function.InstanceFieldAccessFunction;
import org.fuwjin.postage.function.InstanceFieldMutatorFunction;
import org.fuwjin.postage.function.InstanceMethodFunction;

/**
 * A Support factory for instances.
 */
public class InstanceCategory implements FunctionFactory {
   private static void addFields(final FunctionMap map, final Object object, final Class<?> type) {
      if(type != null) {
         for(final Field field: type.getDeclaredFields()) {
            if(!Modifier.isStatic(field.getModifiers())) {
               map.put(field.getName(), new InstanceFieldAccessFunction(field, object));
               map.put(field.getName(), new InstanceFieldMutatorFunction(field, object));
            }
         }
         addFields(map, object, type.getSuperclass());
      }
   }

   /**
    * Adds Functions generated reflectively by the instance methods and fields
    * on type, targeted on the object.
    * @param map the container for generated functions
    * @param object the target object
    * @param type the function generator
    */
   public static void addFunctions(final FunctionMap map, final Object object, final Class<?> type) {
      addMethods(map, object, type);
      addFields(map, object, type);
   }

   private static void addMethods(final FunctionMap map, final Object object, final Class<?> type) {
      if(type != null) {
         for(final Method method: type.getDeclaredMethods()) {
            if(!Modifier.isStatic(method.getModifiers())) {
               map.put(method.getName(), InstanceMethodFunction.method(method, object));
            }
         }
         if(type.isInterface()) {
            for(final Class<?> iface: type.getInterfaces()) {
               addMethods(map, object, iface);
            }
         } else {
            addMethods(map, object, type.getSuperclass());
         }
      }
   }

   private final Object object;
   private final FunctionMap map = new FunctionMap();

   /**
    * Creates a new instance.
    * @param object the instance
    * @param type the type of the object
    */
   public InstanceCategory(final Object object, final Class<?> type) {
      this.object = object;
      map.put("this", new ConstantFunction(object));
      addFunctions(map, object, type);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceCategory o = (InstanceCategory)obj;
         return object.equals(o.object);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Function getFunction(final String name) {
      return map.get(name);
   }

   @Override
   public int hashCode() {
      return object.hashCode();
   }
}
