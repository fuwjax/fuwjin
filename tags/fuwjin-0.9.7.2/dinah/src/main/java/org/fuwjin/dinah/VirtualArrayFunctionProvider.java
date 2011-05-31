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

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.fuwjin.dinah.function.AbstractFunction;
import org.fuwjin.dinah.function.FixedArgsFunction;
import org.fuwjin.util.TypeUtils;

/**
 * Provider which supplies functions abstracting the reflection classes such as
 * Constructor, Method, and Field, in addition to some virtual methods such as
 * primitive and array access and the instanceof keyword.
 */
public class VirtualArrayFunctionProvider extends AbstractFunctionProvider {
   /**
    * Creates a new instance.
    * @param adapter the type converter
    */
   public VirtualArrayFunctionProvider(final Adapter adapter) {
      super(adapter);
   }

   @Override
   public Map<String, AbstractFunction> getFunctions(final String typeName) {
      final Map<String, AbstractFunction> functions = new HashMap<String, AbstractFunction>();
      try {
         final Type type = adapt(typeName, Type.class);
         if(TypeUtils.isArray(type)) {
            addType(functions, typeName, type);
         }
      } catch(final AdaptException e) {
         // continue, return empty functions map
      }
      return functions;
   }

   private void addGet(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      add(functions, new FixedArgsFunction<Member>(this, null, typeName + ".get", type, int.class) {
         @Override
         protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
               IllegalArgumentException, IllegalAccessException, InstantiationException {
            return Array.get(args[0], (Integer)args[1]);
         }
      });
   }

   private void addLen(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      add(functions, new FixedArgsFunction<Member>(this, null, typeName + ".length", type) {
         @Override
         protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
               IllegalArgumentException, IllegalAccessException, InstantiationException {
            return Array.getLength(args[0]);
         }
      });
   }

   private void addNew(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      add(functions, new FixedArgsFunction<Member>(this, null, typeName + ".new", int.class) {
         @Override
         protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
               IllegalArgumentException, IllegalAccessException, InstantiationException {
            return TypeUtils.newArrayInstance(TypeUtils.getComponentType(type), (Integer)args[0]);
         }
      });
   }

   private void addSet(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      add(functions,
            new FixedArgsFunction<Member>(this, null, typeName + ".set", type, int.class, TypeUtils
                  .getComponentType(type)) {
               @Override
               protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
                     IllegalArgumentException, IllegalAccessException, InstantiationException {
                  Array.set(args[0], (Integer)args[1], args[2]);
                  return Adapter.UNSET;
               }
            });
   }

   private void addType(final Map<String, AbstractFunction> functions, final String typeName, final Type type) {
      addNew(functions, typeName, type);
      addGet(functions, typeName, type);
      addSet(functions, typeName, type);
      addLen(functions, typeName, type);
   }
}
