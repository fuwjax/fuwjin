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

import static org.fuwjin.postage.type.ObjectUtils.access;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

public class StaticFieldAccessFunction implements FunctionTarget {
   private final Field field;

   public StaticFieldAccessFunction(final Field field) {
      this.field = field;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final StaticFieldAccessFunction o = (StaticFieldAccessFunction)obj;
         return super.equals(obj) && field.equals(o.field);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      return access(field).get(null);
   }

   @Override
   public Type parameterType(final int index) {
      return null;
   }

   @Override
   public String render(final boolean castArgs, final String... argRepresentations) {
      return field.getDeclaringClass().getCanonicalName() + "." + field.getName();
   }

   @Override
   public int requiredArguments() {
      return 0;
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
