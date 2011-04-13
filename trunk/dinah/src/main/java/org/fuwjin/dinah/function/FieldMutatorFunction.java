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
import java.lang.reflect.Type;

import org.fuwjin.dinah.Function;
import org.fuwjin.util.Adapter;

public class FieldMutatorFunction extends ReflectiveFunction implements Function {
   private final Field field;

   public FieldMutatorFunction(final String typeName, final Field field, final Type type) {
      super(typeName + '.' + field.getName(), type, field.getType());
      this.field = field;
   }

   @Override
   protected Object invokeImpl(final Object[] args) throws InvocationTargetException, Exception {
      field.set(args[0], args[1]);
      return Adapter.unset();
   }

   @Override
   protected Member member() {
      return field;
   }
}
