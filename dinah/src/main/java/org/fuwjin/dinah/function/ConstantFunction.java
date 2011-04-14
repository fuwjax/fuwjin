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
package org.fuwjin.dinah.function;

import java.lang.reflect.Member;

/**
 * Always returns the same value instance. Note that while it is not enforced
 * (or enforcable), it is highly recommended that the value instance be
 * immutable.
 */
public class ConstantFunction extends FixedArgsFunction {
   private final Object value;

   /**
    * Creates a new instance.
    * @param name the name of the function
    * @param value the constant value
    */
   public ConstantFunction(final String name, final Object value) {
      super(name);
      this.value = value;
   }

   @Override
   protected Object invokeSafe(final Object... args) {
      return value;
   }

   @Override
   protected Member member() {
      return null;
   }
}
