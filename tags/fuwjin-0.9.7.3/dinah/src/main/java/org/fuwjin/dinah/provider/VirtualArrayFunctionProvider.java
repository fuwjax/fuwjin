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

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.function.AbstractFunction;
import org.fuwjin.dinah.signature.FixedArgsSignature;
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
   public void addFunctions(final Type type) {
      if(TypeUtils.isArray(type)) {
         add(newFunction(type));
         add(getFunction(type));
         add(setFunction(type));
         add(lenFunction(type));
      }
   }

   private AbstractFunction getFunction(final Type type) {
      return new AbstractFunction(new FixedArgsSignature(this, type, "get", TypeUtils.getComponentType(type), type,
            int.class)) {
         @Override
         protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
               IllegalArgumentException, IllegalAccessException, InstantiationException {
            return Array.get(args[0], (Integer)args[1]);
         }
      };
   }

   private AbstractFunction lenFunction(final Type type) {
      return new AbstractFunction(new FixedArgsSignature(this, type, "length", int.class, type)) {
         @Override
         protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
               IllegalArgumentException, IllegalAccessException, InstantiationException {
            return Array.getLength(args[0]);
         }
      };
   }

   private AbstractFunction newFunction(final Type type) {
      return new AbstractFunction(new FixedArgsSignature(this, type, "new", type, int.class)) {
         @Override
         protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
               IllegalArgumentException, IllegalAccessException, InstantiationException {
            return TypeUtils.newArrayInstance(TypeUtils.getComponentType(type), (Integer)args[0]);
         }
      };
   }

   private AbstractFunction setFunction(final Type type) {
      return new AbstractFunction(new FixedArgsSignature(this, type, "set", void.class, type, int.class,
            TypeUtils.getComponentType(type))) {
         @Override
         protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
               IllegalArgumentException, IllegalAccessException, InstantiationException {
            Array.set(args[0], (Integer)args[1], args[2]);
            return Adapter.UNSET;
         }
      };
   }
}
