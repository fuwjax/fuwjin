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
public class AbortStatement implements Expression {
   private final Expression value;

   /**
    * Creates a new instance.
    * @param value the failure message
    */
   public AbortStatement(final Expression value) {
      this.value = value;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final Snapshot snapshot = new Snapshot(input, output, scope);
      try {
         final Object val = value.resolve(input, output, scope);
         throw new AbortedException("%s: %s", val, snapshot);
      } catch(final ResolveException e) {
         throw new AbortedException(e, "Abort string could not be generated: %s", snapshot);
      }
   }

   @Override
   public String toString() {
      return "abort " + value;
   }

   public Expression value() {
      return value;
   }
}
