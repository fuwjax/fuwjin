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

public class InstanceFieldMutatorFunction implements FunctionTarget {
   private final Field field;
   private final Object target;

   public InstanceFieldMutatorFunction(final Field field, final Object target) {
      this.field = field;
      this.target = target;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceFieldMutatorFunction o = (InstanceFieldMutatorFunction)obj;
         return super.equals(obj) && field.equals(o.field) && target.equals(o.target);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      access(field).set(target, args[0]);
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
      throw new UnsupportedOperationException();
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
