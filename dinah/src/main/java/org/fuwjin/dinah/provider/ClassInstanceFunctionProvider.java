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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.function.ConstantFunction;
import org.fuwjin.dinah.function.InstanceFieldAccessFunction;
import org.fuwjin.dinah.function.InstanceFieldMutatorFunction;
import org.fuwjin.dinah.function.InstanceMethodFunction;

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
   public void addFunctions(final Type type) {
      add(new ConstantFunction(this, type, "this", type));
      addMethods(type);
      addFields(type);
   }

   private void addFields(final Type type) {
      for(final Field field: Class.class.getDeclaredFields()) {
         if(access(field) && !Modifier.isStatic(field.getModifiers())) {
            add(new InstanceFieldAccessFunction(this, type, field, type));
            add(new InstanceFieldMutatorFunction(this, type, field, type));
         }
      }
   }

   private void addMethods(final Type type) {
      for(final Method method: Class.class.getDeclaredMethods()) {
         if(access(method) && !Modifier.isStatic(method.getModifiers())) {
            add(new InstanceMethodFunction(this, type, method, type));
         }
      }
   }
}
