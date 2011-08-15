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
 * Represents an unrecoverable failure.
 */
public class ConditionalAbortStatement implements Expression {
   private final Expression statement;
   private final Expression value;

   /**
    * Creates a new instance.
    * @param value the failure message
    */
   public ConditionalAbortStatement(final Expression statement, final Expression value) {
      this.statement = statement;
      this.value = value;
   }

   @Override
   public Object resolve(final Trace trace)
         throws AbortedException, ResolveException {
      try {
         return statement.resolve(trace);
      } catch(final ResolveException e) {
         try {
            final Object val = value.resolve(trace);
            return trace.abort(e, "%s", val);
         } catch(final ResolveException ex) {
            return trace.abort(e, "Abort string could not be generated");
         }
      }
   }

   public Expression statement() {
      return statement;
   }

   @Override
   public String toString() {
      return "abort " + value;
   }

   /**
    * Returns the abort message.
    * @return the message
    */
   public Expression value() {
      return value;
   }
}
