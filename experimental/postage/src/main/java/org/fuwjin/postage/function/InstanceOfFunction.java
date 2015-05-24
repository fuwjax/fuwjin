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

public class InstanceOfFunction implements FunctionTarget {
   private final Class<?> type;

   public InstanceOfFunction(final Class<?> type) {
      this.type = type;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceOfFunction o = (InstanceOfFunction)obj;
         return super.equals(obj) && type.equals(o.type);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      return type.isInstance(args[0]);
   }

   @Override
   public Type parameterType(final int index) {
      if(index == 0) {
         return Object.class;
      }
      return null;
   }

   @Override
   public String render(final boolean castArgs, final String... argRepresentations) {
      return argRepresentations[0] + " instanceof " + type.getCanonicalName();
   }

   @Override
   public int requiredArguments() {
      return 1;
   }

   @Override
   public Type returnType() {
      return boolean.class;
   }

   @Override
   public String toString() {
      return type.getCanonicalName();
   }
}
