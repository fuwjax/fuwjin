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

import static org.fuwjin.postage.type.TypeUtils.getCanonicalName;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.FunctionTarget;
import org.fuwjin.postage.type.ObjectUtils;

public class FieldMutatorFunction implements FunctionTarget {
   private static Field findField(final Class<?> type, final String name) {
      Class<?> cls = type;
      while(cls != null) {
         for(final Field f: cls.getDeclaredFields()) {
            if(f.getName().equals(name)) {
               return f;
            }
         }
         cls = cls.getSuperclass();
      }
      throw new IllegalArgumentException("No field " + name + " on " + type);
   }

   private final Field field;
   private final Class<?> firstParam;

   public FieldMutatorFunction(final Class<?> type, final String name) {
      field = findField(type, name);
      firstParam = type;
   }

   public FieldMutatorFunction(final Field field, final Class<?> firstParam) {
      this.field = field;
      this.firstParam = firstParam;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final FieldMutatorFunction o = (FieldMutatorFunction)obj;
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
      ObjectUtils.access(field).set(args[0], args[1]);
      return null;
   }

   @Override
   public Type parameterType(final int index) {
      if(index == 0) {
         return firstParam;
      }
      if(index == 1) {
         return field.getType();
      }
      return null;
   }

   @Override
   public String render(final boolean castArgs, final String... argRepresentations) {
      final StringBuilder builder = new StringBuilder();
      if(castArgs) {
         builder.append("((").append(getCanonicalName(parameterType(0))).append(")");
         builder.append(argRepresentations[0]).append(")");
      } else {
         builder.append(argRepresentations[0]);
      }
      builder.append(".").append(field.getName()).append(" = ").append("(").append(getCanonicalName(parameterType(0)))
            .append(")").append(argRepresentations[1]);
      return builder.toString();
   }

   @Override
   public int requiredArguments() {
      return 2;
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
