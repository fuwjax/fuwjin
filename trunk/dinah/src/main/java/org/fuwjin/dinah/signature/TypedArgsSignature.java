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
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.dinah.SignatureConstraint;

/**
 * Abstraction over a Function's name and argument types.
 */
public class TypedArgsSignature extends ConstraintDecorator {
   private final Type[] args;

   /**
    * Creates a new instance. The number of arguments is indeterminate without
    * subsequent calls to addArg.
    * @param name the function name
    */
   public TypedArgsSignature(final SignatureConstraint constraint, final Type... types) {
      super(constraint);
      args = types;
   }

   @Override
   public boolean matches(final FunctionSignature signature) {
      return super.matches(signature) && signature.canAdapt(args);
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append(name());
      String delim = "(";
      for(final Type type: args) {
         builder.append(delim).append(type);
         delim = ", ";
      }
      return builder.append(')').toString();
   }
}
