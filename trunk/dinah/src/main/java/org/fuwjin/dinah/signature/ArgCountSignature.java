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

import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.dinah.SignatureConstraint;

/**
 * Abstraction over a Function's name and argument types.
 */
public class ArgCountSignature extends ConstraintDecorator {
   private final int count;

   /**
    * Creates a new instance.
    * @param name the function name
    * @param argCount the number of arguments
    */
   public ArgCountSignature(final SignatureConstraint constraint, final int argCount) {
      super(constraint);
      count = argCount;
   }

   @Override
   public boolean matches(final FunctionSignature signature) {
      return super.matches(signature) && signature.supportsArgs(count);
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append(name());
      String delim = "(";
      if(count == 0) {
         builder.append(delim);
      } else {
         for(int i = 0; i < count; i++) {
            builder.append(delim).append("?");
            delim = ", ";
         }
      }
      return builder.append(')').toString();
   }
}
