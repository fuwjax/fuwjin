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
import org.fuwjin.dinah.FunctionSignature;

/**
 * Abstraction over a Function's name and argument types.
 */
public class ArgCountSignature extends NameOnlySignature {
   private final int count;

   /**
    * Creates a new instance.
    * @param name the function name
    * @param argCount the number of arguments
    */
   public ArgCountSignature(final String name, final int argCount) {
      super(name);
      count = argCount;
   }

   @Override
   public FunctionSignature accept(final int paramCount) {
      if(count() != paramCount) {
         throw new IllegalArgumentException();
      }
      return this;
   }

   @Override
   public boolean matchesFixed(final Type... params) {
      return params.length == count();
   }

   @Override
   public boolean matchesVarArgs(final Type... params) {
      return params.length <= count() + 1;
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append(name());
      String delim = "(";
      if(count() == 0) {
         builder.append(delim);
      } else {
         for(int i = 0; i < count(); i++) {
            builder.append(delim).append("?");
            delim = ", ";
         }
      }
      return builder.append(')').toString();
   }

   protected int count() {
      return count;
   }
}
