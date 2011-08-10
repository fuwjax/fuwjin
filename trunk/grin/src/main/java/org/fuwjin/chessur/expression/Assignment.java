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

import org.fuwjin.dinah.Adapter;
import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;
import org.fuwjin.grin.env.Trace;

/**
 * Represents an assignment.
 */
public class Assignment implements Expression {
   private final String name;
   private final Expression value;

   /**
    * Creates a new instance.
    * @param name the variable name
    * @param value the new value
    */
   public Assignment(final String name, final Expression value) {
      this.name = name;
      this.value = value;
   }

   /**
    * Returns the variable name.
    * @return the variable
    */
   public String name() {
      return name;
   }

   @Override
   public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
         throws AbortedException, ResolveException {
      try {
         final Object result = value.resolve(input, output, scope, trace);
         scope.put(name, result);
         return Adapter.UNSET;
      } catch(final ResolveException e) {
         throw trace.fail(e, "could not assign to %s", name);
      }
   }

   @Override
   public String toString() {
      return name + " = " + value;
   }

   /**
    * Returns the assigned value.
    * @return the assigned value
    */
   public Expression value() {
      return value;
   }
}
