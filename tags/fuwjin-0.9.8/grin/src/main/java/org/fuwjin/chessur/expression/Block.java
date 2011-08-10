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

import static java.util.Collections.unmodifiableCollection;
import java.util.ArrayList;
import java.util.List;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;
import org.fuwjin.grin.env.Trace;

/**
 * Represents a sequence of statements.
 */
public class Block implements Expression {
   private final List<Expression> statements = new ArrayList<Expression>();

   /**
    * Adds a statement to the block.
    * @param statement the next statement
    */
   public void add(final Expression statement) {
      statements.add(statement);
   }

   @Override
   public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
         throws AbortedException, ResolveException {
      for(final Expression statement: statements) {
         statement.resolve(input, output, scope, trace);
      }
      return Adapter.UNSET;
   }

   /**
    * Returns the list of statements.
    * @return the list of statements
    */
   public Iterable<Expression> statements() {
      return unmodifiableCollection(statements);
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder("{");
      for(final Expression statement: statements) {
         builder.append("\n  ").append(statement);
      }
      return builder.append("\n}").toString();
   }
}
