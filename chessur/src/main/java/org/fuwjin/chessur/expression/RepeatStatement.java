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
import org.fuwjin.util.Adapter;

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
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      Snapshot snapshot = new Snapshot(input, output, scope);
      //statement.resolve(input, output, scope);
      snapshot.resolve(statement, true);
      try {
         while(true) {
            snapshot = new Snapshot(input, output, scope);
            snapshot.resolve(statement, true);
         }
      } catch(final ResolveException e) {
         // continue
      }
      return Adapter.unset();
   }

   @Override
   public String toString() {
      return "repeat " + statement;
   }
}
