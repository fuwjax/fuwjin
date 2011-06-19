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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.dinah.signature.VarArgsSignature;

/**
 * Function for reflective constructor execution.
 */
public class ConstructorFunction extends MemberFunction<Constructor<?>> {
   /**
    * Creates a new instance.
    * @param adapter the type converter
    * @param category the function category
    * @param constructor the constructor
    */
   public ConstructorFunction(final Adapter adapter, final Type category, final Constructor<?> constructor) {
      super(constructor, VarArgsSignature.newSignature(adapter, category, "new", category,
            constructor.getParameterTypes(), constructor.isVarArgs()));
   }

   @Override
   protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
         IllegalArgumentException, IllegalAccessException, InstantiationException {
      return member().newInstance(args);
   }
}
