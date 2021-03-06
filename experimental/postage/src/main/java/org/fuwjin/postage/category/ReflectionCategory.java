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

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.FunctionFactory;

/**
 * The proper factory for Class-based reflection.
 */
public class ReflectionCategory implements FunctionFactory {
   private final Map<String, FunctionFactory> categories = new HashMap<String, FunctionFactory>();

   @Override
   public Function getFunction(final String name) {
      final int index = name.lastIndexOf('.');
      if(index < 0) {
         return null;
      }
      final String category = name.substring(0, index);
      FunctionFactory cat = categories.get(category);
      if(cat == null) {
         try {
            final Class<?> type = Class.forName(category);
            cat = new ClassCategory(type);
            categories.put(category, cat);
         } catch(final ClassNotFoundException e) {
            return null;
         }
      }
      return cat.getFunction(name.substring(index + 1));
   }
}
