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
package org.fuwjin.dinah.function;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;

import org.fuwjin.util.Adapter;

public class StaticFieldMutatorFunction extends ReflectiveFunction {
   private final Field field;

   public StaticFieldMutatorFunction(final String typeName, final Field field) {
      super(typeName + '.' + field.getName(), field.getType());
      this.field = field;
   }

   @Override
   protected Object invokeImpl(final Object[] args) throws InvocationTargetException, Exception {
      field.set(null, args[0]);
      return Adapter.unset();
   }

   @Override
   protected Member member() {
      return field;
   }
}
