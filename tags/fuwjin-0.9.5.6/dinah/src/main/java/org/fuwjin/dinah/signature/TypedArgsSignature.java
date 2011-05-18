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
package org.fuwjin.dinah.signature;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.fuwjin.util.TypeUtils;

/**
 * Abstraction over a Function's name and argument types.
 */
public class TypedArgsSignature extends ArgCountSignature {
   private final List<Type> args = new ArrayList<Type>();

   /**
    * Creates a new instance. The number of arguments is indeterminate without
    * subsequent calls to addArg.
    * @param name the function name
    */
   public TypedArgsSignature(final String name) {
      super(name, 0);
   }

   /**
    * Adds an argument type to the argument type list.
    * @param typeName the name of the argument type
    * @return this function signature, for chaining
    * @throws ClassNotFoundException if the typeName does not map to a type
    */
   public TypedArgsSignature addArg(final String typeName) throws ClassNotFoundException {
      if(typeName == null || "null".equals(typeName)) {
         args.add(null);
      } else {
         args.add(TypeUtils.forName(typeName));
      }
      return this;
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

   @Override
   public boolean matchesFixed(final Type... params) {
      if(!super.matchesFixed(params)) {
         return false;
      }
      for(int i = 0; i < params.length; ++i) {
         if(!TypeUtils.isAssignableFrom(params[i], argType(i))) {
            return false;
         }
      }
      return true;
   }

   @Override
   public boolean matchesVarArgs(final Type... params) {
      if(matchesFixed(params)) {
         return true;
      }
      if(!super.matchesVarArgs(params)) {
         return false;
      }
      for(int i = 0; i < params.length - 1; ++i) {
         if(!TypeUtils.isAssignableFrom(params[i], argType(i))) {
            return false;
         }
      }
      final Type componentType = TypeUtils.getComponentType(params[params.length - 1]);
      for(int index = params.length - 1; index < count(); ++index) {
         if(!TypeUtils.isAssignableFrom(componentType, argType(index))) {
            return false;
         }
      }
      return true;
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append(name());
      String delim = "(";
      if(count() == 0) {
         builder.append(delim);
      } else {
         for(final Type type: args) {
            builder.append(delim).append(TypeUtils.getName(type));
            delim = ", ";
         }
      }
      return builder.append(')').toString();
   }

   /**
    * Returns the number of arguments.
    * @return the number of arguments
    */
   @Override
   protected int count() {
      return args.size();
   }
}
