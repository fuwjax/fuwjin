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
package org.fuwjin.pogo.postage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Iterator;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.FunctionTarget;
import org.fuwjin.postage.category.AbstractCategory;
import org.fuwjin.postage.category.FunctionMap;
import org.fuwjin.postage.function.ConstantFunction;

/**
 * A Postage Category of the base functions required by Pogo.
 */
public class PogoCategory extends AbstractCategory {
   private final FunctionMap map = new FunctionMap();

   /**
    * Creates a new instance.
    */
   public PogoCategory() {
      super(null);
      addFunction("default", new ConstantFunction(true, boolean.class));
      addFunction("true", new ConstantFunction(Boolean.TRUE));
      addFunction("null", new ConstantFunction(null));
      addFunction("false", new ConstantFunction(Boolean.FALSE));
      addFunction("return", new ConstantFunction(PostageUtils.Return.RETURN));
      addFunction("this", new FunctionTarget() {
         @Override
         public boolean equals(final Object obj) {
            try {
               return getClass().equals(obj.getClass());
            } catch(final RuntimeException e) {
               return false;
            }
         }

         @Override
         public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
            return args[0];
         }

         @Override
         public Type parameterType(final int index) {
            return Object.class;
         }

         @Override
         public int requiredArguments() {
            return 1;
         }

         @Override
         public Type returnType() {
            return Object.class;
         }
      });
      addFunction("next", new FunctionTarget() {
         @Override
         public boolean equals(final Object obj) {
            try {
               return getClass().equals(obj.getClass());
            } catch(final RuntimeException e) {
               return false;
            }
         }

         @Override
         public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
            return ((Iterator<?>)args[0]).next();
         }

         @Override
         public Type parameterType(final int index) {
            return Iterator.class;
         }

         @Override
         public int requiredArguments() {
            return 1;
         }

         @Override
         public Type returnType() {
            return Object.class;
         }
      });
   }

   /**
    * Adds a function to the category.
    * @param name the name of the function
    * @param function the function
    */
   private void addFunction(final String name, final FunctionTarget function) {
      map.put(name, function);
   }

   @Override
   public Function getTargetFunction(final String name) {
      return map.get(name);
   }
}
