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

import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;

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
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final Snapshot snapshot = new Snapshot(input, output, scope);
      try {
         return statement.resolve(input, output, scope);
      } catch(final ResolveException e) {
         throw abort(snapshot, e);
      } catch(final AbortedException e) {
         throw abort(snapshot, e);
      }
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

   protected AbortedException abort(final Snapshot snapshot, final Throwable cause) throws AbortedException {
      try {
         final Object val = snapshot.resolve(value, true);
         return new AbortedException(cause, "%s: %s", val, snapshot);
      } catch(final ResolveException e) {
         return new AbortedException(cause, "Abort string could not be generated: %s", snapshot);
      }
   }
}
