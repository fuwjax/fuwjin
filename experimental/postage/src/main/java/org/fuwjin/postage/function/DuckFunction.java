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
import java.util.HashMap;
import java.util.Map;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.FunctionTarget;
import org.fuwjin.postage.category.ClassCategory;

public class DuckFunction implements FunctionTarget {
   private final Map<Class<?>, ClassCategory> classes = new HashMap<Class<?>, ClassCategory>();
   private final String name;

   public DuckFunction(final String name) {
      this.name = name;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final DuckFunction o = (DuckFunction)obj;
         return getClass().equals(o.getClass()) && name.equals(o.name);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return name.hashCode();
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      if(args.length == 0) {
         return new Failure("Target object required to duck type");
      }
      if(args[0] == null) {
         return new Failure("Non-null object required to duck type");
      }
      ClassCategory category = classes.get(args[0].getClass());
      if(category == null) {
         category = new ClassCategory(args[0].getClass(), false);
         classes.put(args[0].getClass(), category);
      }
      return category.getFunction(name).invokeSafe(args);
   }

   @Override
   public Type parameterType(final int index) {
      return Object.class;
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
      return Object.class;
   }
}
