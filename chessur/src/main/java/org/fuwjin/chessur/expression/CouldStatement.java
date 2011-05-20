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
import org.fuwjin.dinah.Adapter;

/**
 * Represents a statement that always succeeds.
 */
public class CouldStatement implements Expression {
   private final Expression statement;

   /**
    * Creates a new instance.
    * @param statement the possible statement
    */
   public CouldStatement(final Expression statement) {
      this.statement = statement;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException {
      final Snapshot snapshot = new Snapshot(input, output, scope);
      try {
         return snapshot.resolve(statement, true);
      } catch(final ResolveException e) {
         return Adapter.UNSET;
      }
   }

   /**
    * Returns the optional statement.
    * @return the optional statement
    */
   public Expression statement() {
      return statement;
   }

   @Override
   public String toString() {
      return "could " + statement;
   }
}
