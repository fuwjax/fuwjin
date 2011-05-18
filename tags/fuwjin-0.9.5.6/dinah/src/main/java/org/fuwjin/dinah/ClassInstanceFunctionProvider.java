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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.fuwjin.dinah.adapter.StandardAdapter;
import org.fuwjin.dinah.function.AbstractFunction;
import org.fuwjin.dinah.function.InstanceFieldAccessFunction;
import org.fuwjin.dinah.function.InstanceFieldMutatorFunction;
import org.fuwjin.dinah.function.InstanceMethodFunction;
import org.fuwjin.dinah.function.VarArgsFunction;
import org.fuwjin.util.TypeUtils;

/**
 * Provider which supplies functions abstracting the reflection classes such as
 * Constructor, Method, and Field, in addition to some virtual methods such as
 * primitive and array access and the instanceof keyword.
 */
public class ClassInstanceFunctionProvider extends AbstractFunctionProvider {
   private final Adapter adapter;

   public ClassInstanceFunctionProvider() {
      this(new StandardAdapter());
   }

   public ClassInstanceFunctionProvider(final Adapter adapter) {
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

   private void addFields(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      for(final Field field: Class.class.getDeclaredFields()) {
         if(access(field) && !Modifier.isStatic(field.getModifiers())) {
            add(functions, new InstanceFieldAccessFunction(adapter, typeName, field, type));
            add(functions, new InstanceFieldMutatorFunction(adapter, typeName, field, type));
         }
      }
   }

   private void addMethods(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      for(final Method method: Class.class.getDeclaredMethods()) {
         if(access(method) && !Modifier.isStatic(method.getModifiers())) {
            if(method.isVarArgs()) {
               add(functions, new VarArgsFunction(adapter, new InstanceMethodFunction(adapter, typeName, method, type)));
            } else {
               add(functions, new InstanceMethodFunction(adapter, typeName, method, type));
            }
         }
      }
   }

   private void addType(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      addMethods(functions, typeName, type);
      addFields(functions, typeName, type);
   }
}
