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
package org.fuwjin.postage.category;

import static org.fuwjin.postage.category.InstanceCategory.addFunctions;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.FunctionFactory;
import org.fuwjin.postage.function.ConstantFunction;

/**
 * A Support factory for instances.
 */
public class NullCategory implements FunctionFactory {
   private static final Object NULL = new Object() {
      @Override
      public boolean equals(final Object obj) {
         return obj == null;
      }

      @Override
      public int hashCode() {
         return 0;
      }

      @Override
      public String toString() {
         return "null";
      }
   };
   private final FunctionMap map = new FunctionMap();

   /**
    * Creates a new instance.
    */
   public NullCategory() {
      map.put("this", new ConstantFunction(null));
      addFunctions(map, NULL, Object.class);
   }

   @Override
   public boolean equals(final Object obj) {
      return getClass().equals(obj.getClass());
   }

   @Override
   public Function getFunction(final String name) {
      return map.get(name);
   }

   @Override
   public int hashCode() {
      return 0;
   }
}
