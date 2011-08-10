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
 * Represents a statement that repeats one or more times.
 */
public class RepeatStatement implements Expression {
   private final Expression statement;

   /**
    * Creates a new instance.
    * @param statement the repeated statement
    */
   public RepeatStatement(final Expression statement) {
      this.statement = statement;
   }

   @Override
   public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
         throws AbortedException, ResolveException {
      statement.resolve(input, output, scope, trace);
      try {
         while(true) {
            trace.resolve(statement);
         }
      } catch(final ResolveException e) {
         // continue
      }
      return Adapter.UNSET;
   }

   /**
    * Returns the repeated statement.
    * @return the statement
    */
   public Expression statement() {
      return statement;
   }

   @Override
   public String toString() {
      return "repeat " + statement;
   }
}
