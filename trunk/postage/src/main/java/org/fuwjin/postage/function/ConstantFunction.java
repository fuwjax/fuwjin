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
package org.fuwjin.postage.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

/**
 * Creates a new target backed by a value.
 */
public class ConstantFunction implements FunctionTarget {
   private final Object value;
   private final Class<?> type;

   /**
    * Creates a new instance.
    * @param value the constant value
    */
   public ConstantFunction(final Object value) {
      this.value = value;
      type = value == null ? Object.class : value.getClass();
   }

   public ConstantFunction(final Object value, final Class<?> type) {
      this.value = value;
      this.type = type;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ConstantFunction o = (ConstantFunction)obj;
         return getClass().equals(o.getClass()) && type.equals(o.type)
               && (value == null ? o.value == null : value.equals(o.value));
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return type.hashCode();
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      return value;
   }

   @Override
   public Type parameterType(final int index) {
      return Object.class;
   }

   @Override
   public int requiredArguments() {
      return 0;
   }

   @Override
   public Type returnType() {
      return type;
   }
}
