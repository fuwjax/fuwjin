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
package org.fuwjin.chessur.expression;

import org.fuwjin.grin.env.Trace;


/**
 * An expression representing an untyped number.
 */
public class Number extends java.lang.Number implements Expression {
   private static final long serialVersionUID = 1L;
   private final String value;

   /**
    * Creates a new instance.
    * @param value the string value of the number
    */
   public Number(final String value) {
      this.value = value;
   }

   @Override
   public double doubleValue() {
      return Double.parseDouble(value);
   }

   @Override
   public float floatValue() {
      return Float.parseFloat(value);
   }

   @Override
   public int intValue() {
      return Integer.parseInt(value);
   }

   @Override
   public long longValue() {
      return Long.parseLong(value);
   }

   @Override
   public Object resolve(final Trace trace)
         throws AbortedException, ResolveException {
      return this;
   }

   @Override
   public String toString() {
      return value;
   }
}
