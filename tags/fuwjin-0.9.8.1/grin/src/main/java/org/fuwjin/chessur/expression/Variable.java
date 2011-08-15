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

import org.fuwjin.dinah.adapter.StandardAdapter;
import org.fuwjin.grin.env.Trace;

/**
 * Represents a variable in the current scope.
 */
public class Variable implements Expression {
   /**
    * Represents the next character.
    */
   public static final Variable NEXT = new Variable("input:next") {
      @Override
      public Object resolve(final Trace trace) throws AbortedException, ResolveException {
         return trace.next();
      }
   };
   private final String name;

   /**
    * Creates a new instance.
    * @param name the variable name
    */
   public Variable(final String name) {
      this.name = name;
   }

   /**
    * Returns the name of the variable.
    * @return the name
    */
   public String name() {
      return name;
   }

   @Override
   public Object resolve(final Trace trace) throws AbortedException, ResolveException {
      final Object value = trace.get(name);
      if(StandardAdapter.isSet(value)) {
         return value;
      }
      throw trace.fail("variable %s is unset", name);
   }

   @Override
   public String toString() {
      return name;
   }
}
