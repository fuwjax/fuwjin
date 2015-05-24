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
import static org.fuwjin.postage.type.TypeUtils.getCanonicalName;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.FunctionTarget;

public class StaticFieldMutatorFunction implements FunctionTarget {
   private final Field field;

   public StaticFieldMutatorFunction(final Field field) {
      this.field = field;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final StaticFieldMutatorFunction o = (StaticFieldMutatorFunction)obj;
         return super.equals(obj) && field.equals(o.field);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      access(field).set(null, args[0]);
      return null;
   }

   @Override
   public Type parameterType(final int index) {
      if(index == 0) {
         return field.getType();
      }
      return null;
   }

   @Override
   public String render(final boolean castArgs, final String... argRepresentations) {
      final StringBuilder builder = new StringBuilder();
      builder.append(field.getDeclaringClass().getCanonicalName()).append(".").append(field.getName()).append(" = ");
      if(castArgs) {
         builder.append("(").append(getCanonicalName(parameterType(0))).append(")");
      }
      return builder.append(argRepresentations[0]).toString();
   }

   @Override
   public int requiredArguments() {
      return 1;
   }

   @Override
   public Type returnType() {
      return void.class;
   }

   @Override
   public String toString() {
      return field.toString();
   }
}
