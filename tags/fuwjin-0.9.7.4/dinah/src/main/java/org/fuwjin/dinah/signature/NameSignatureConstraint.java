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
public class NameSignatureConstraint implements SignatureConstraint {
   private final String name;
   private final String category;

   /**
    * Creates a new instance. The number of arguments is indeterminate without
    * subsequent calls to addArg.
    * @param name the function name
    */
   public NameSignatureConstraint(final String category, final String name) {
      this.category = category;
      this.name = name;
   }

   @Override
   public String category() {
      return category;
   }

   @Override
   public boolean matches(final FunctionSignature signature) {
      return signature.name().equals(name);
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public String toString() {
      return name() + "(*)";
   }
}
