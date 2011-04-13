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

public class StaticFieldAccessFunction extends ReflectiveFunction {
   private final Field field;

   public StaticFieldAccessFunction(final String typeName, final Field field) {
      super(typeName + '.' + field.getName());
      this.field = field;
   }

   @Override
   protected Object invokeImpl(final Object[] args) throws InvocationTargetException, Exception {
      return field.get(null);
   }

   @Override
   protected Member member() {
      return field;
   }
}
