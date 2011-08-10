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
 * Represents a set of ordered options.
 */
public class EitherOrStatement implements Expression {
   private final List<Expression> statements = new ArrayList<Expression>();

   /**
    * Creates a new instance.
    * @param statement the "either" statement
    */
   public EitherOrStatement(final Expression statement) {
      statements.add(statement);
   }

   /**
    * Adds a statement to the block.
    * @param statement the next option
    * @return this statement
    */
   public EitherOrStatement or(final Expression statement) {
      statements.add(statement);
      return this;
   }

   @Override
   public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
         throws AbortedException, ResolveException {
      int index = statements.size();
      for(final Expression statement: statements) {
         if(--index == 0) {
            return statement.resolve(input, output, scope, trace);
         }
         try {
            return trace.resolve(statement);
         } catch(final ResolveException e) {
            // continue
         }
      }
      return Adapter.UNSET;
   }

   /**
    * Returns the list of options.
    * @return the options
    */
   public Iterable<Expression> statements() {
      return unmodifiableCollection(statements);
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder("either ");
      for(final Expression statement: statements) {
         builder.append("\nor ").append(statement);
      }
      return builder.toString();
   }
}
