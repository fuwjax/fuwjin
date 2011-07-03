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
public abstract class ConstraintDecorator implements SignatureConstraint {
   private final SignatureConstraint constraint;

   /**
    * Creates a new instance. The number of arguments is indeterminate without
    * subsequent calls to addArg.
    * @param name the function name
    */
   public ConstraintDecorator(final SignatureConstraint constraint) {
      this.constraint = constraint;
   }

   @Override
   public String category() {
      return constraint.category();
   }

   @Override
   public boolean matches(final FunctionSignature signature) {
      return constraint.matches(signature);
   }

   @Override
   public String name() {
      return constraint.name();
   }
}
