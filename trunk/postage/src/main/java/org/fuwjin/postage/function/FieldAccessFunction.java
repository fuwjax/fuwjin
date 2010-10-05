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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.FunctionTarget;
import org.fuwjin.postage.type.ObjectUtils;

public class FieldAccessFunction implements FunctionTarget {
   private final Field field;
   private final Class<?> firstParam;

   public FieldAccessFunction(final Field field, final Class<?> firstParam) {
      this.field = field;
      this.firstParam = firstParam;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final FieldAccessFunction o = (FieldAccessFunction)obj;
         return super.equals(obj) && field.equals(o.field) && firstParam.equals(o.firstParam);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      if(!firstParam.isInstance(args[0])) {
         return new Failure("Target must be %s, not %s", firstParam, args[0].getClass());
      }
      return ObjectUtils.access(field).get(args[0]);
   }

   @Override
   public Type parameterType(final int index) {
      if(index == 0) {
         return firstParam;
      }
      return null;
   }

   @Override
   public int requiredArguments() {
      return 1;
   }

   @Override
   public Type returnType() {
      return field.getType();
   }

   @Override
   public String toString() {
      return field.toString();
   }
}
