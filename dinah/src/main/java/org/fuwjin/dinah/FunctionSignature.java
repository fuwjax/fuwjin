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
package org.fuwjin.dinah;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.fuwjin.util.TypeUtils;

/**
 * Abstraction over a Function's name and argument types.
 */
public class FunctionSignature {
   private final String name;
   private final List<Type> args = new ArrayList<Type>();
   private int count = -1;

   /**
    * Creates a new instance. The number of arguments is indeterminate without
    * subsequent calls to addArg.
    * @param name the function name
    */
   public FunctionSignature(final String name) {
      this(name, -1);
   }

   /**
    * Creates a new instance.
    * @param name the function name
    * @param argCount the number of arguments
    */
   public FunctionSignature(final String name, final int argCount) {
      this.name = name;
      count = argCount;
   }

   /**
    * Adds an argument type to the argument type list.
    * @param typeName the name of the argument type
    * @return this function signature, for chaining
    * @throws ClassNotFoundException if the typeName does not map to a type
    */
   public FunctionSignature addArg(final String typeName) throws ClassNotFoundException {
      if(typeName == null || "null".equals(typeName)) {
         args.add(null);
      } else {
         args.add(TypeUtils.forName(typeName));
      }
      return this;
   }

   /**
    * Returns the number of arguments.
    * @return the number of arguments
    */
   public int argCount() {
      if(count == -1) {
         return args.size();
      }
      return count;
   }

   /**
    * Returns the argument type for the given argument index.
    * @param index the argument index
    * @return the argument type
    */
   public Type argType(final int index) {
      if(index < args.size()) {
         return args.get(index);
      }
      return null;
   }

   /**
    * Returns the category prefix of the name.
    * @return the category prefix
    */
   public String category() {
      final int index = name.lastIndexOf('.');
      if(index < 0) {
         throw new IllegalArgumentException(name + " is not a valid function name");
      }
      return name.substring(0, index);
   }

   /**
    * Returns the name (category.id).
    * @return the name
    */
   public String name() {
      return name;
   }

   /**
    * Sets the number of arguments.
    * @param size the new number of arguments
    */
   public void setArgCount(final int size) {
      if(args.size() > size) {
         throw new IllegalArgumentException("count " + size + " must be at least " + args.size());
      }
      count = size;
   }

   @Override
   public String toString() {
      return name;
   }
}
