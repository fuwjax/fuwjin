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

/**
 * Abstraction over a Function's name and argument types.
 */
public class NameOnlySignature implements FunctionSignature {
   private final String name;

   /**
    * Creates a new instance. The number of arguments is indeterminate without
    * subsequent calls to addArg.
    * @param name the function name
    */
   public NameOnlySignature(final String name) {
      this.name = name;
   }

   @Override
   public FunctionSignature accept(final int paramCount) {
      return new ArgCountSignature(name, paramCount);
   }

   @Override
   public String category() {
      final int index = name.lastIndexOf('.');
      return name.substring(0, index);
   }

   @Override
   public boolean matchesFixed(final Type... params) {
      return true;
   }

   @Override
   public boolean matchesVarArgs(final Type... params) {
      return true;
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public String toString() {
      return name;
   }
}
