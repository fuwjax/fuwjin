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
import org.fuwjin.dinah.function.AbstractFunction;
import org.fuwjin.dinah.function.ConstantFunction;
import org.fuwjin.dinah.function.InstanceFieldAccessFunction;
import org.fuwjin.dinah.function.InstanceFieldMutatorFunction;
import org.fuwjin.dinah.function.InstanceMethodFunction;
import org.fuwjin.dinah.function.VarArgsFunction;

/**
 * Provider which supplies functions provided by a Class instance.
 */
public class ClassInstanceFunctionProvider extends AbstractFunctionProvider {
   /**
    * Creates a new instance.
    * @param adapter the type converter
    */
   public ClassInstanceFunctionProvider(final Adapter adapter) {
      super(adapter);
   }

   @Override
   public Map<String, AbstractFunction> getFunctions(final String typeName) {
      final Map<String, AbstractFunction> functions = new HashMap<String, AbstractFunction>();
      try {
         final Type type = adapt(typeName, Type.class);
         addType(functions, typeName, type);
      } catch(final AdaptException e) {
         // continue, return empty functions map
      }
      return functions;
   }

   private void addFields(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      for(final Field field: Class.class.getDeclaredFields()) {
         if(access(field) && !Modifier.isStatic(field.getModifiers())) {
            add(functions, new InstanceFieldAccessFunction(this, typeName, field, type));
            add(functions, new InstanceFieldMutatorFunction(this, typeName, field, type));
         }
      }
   }

   private void addMethods(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      for(final Method method: Class.class.getDeclaredMethods()) {
         if(access(method) && !Modifier.isStatic(method.getModifiers())) {
            if(method.isVarArgs()) {
               add(functions, new VarArgsFunction(this, new InstanceMethodFunction(this, typeName, method, type)));
            } else {
               add(functions, new InstanceMethodFunction(this, typeName, method, type));
            }
         }
      }
   }

   private void addType(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      add(functions, new ConstantFunction(typeName + ".this", type));
      addMethods(functions, typeName, type);
      addFields(functions, typeName, type);
   }
}
