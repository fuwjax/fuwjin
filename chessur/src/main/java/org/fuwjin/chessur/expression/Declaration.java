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
 * Represents a Specification declaration.
 */
public class Declaration implements Expression {
   private final String name;
   private final Block block;
   private Expression returns;

   /**
    * Creates a new instance.
    * @param name the specification name
    */
   public Declaration(final String name) {
      this.name = name;
      block = new Block();
   }

   /**
    * Adds a statement to the declaration block.
    * @param statement the next statement
    */
   public void add(final Expression statement) {
      block.add(statement);
   }

   /**
    * Returns the specification name.
    * @return the specification name
    */
   public String name() {
      return name;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws ResolveException, AbortedException {
      final SourceStream in = input.mark();
      final Snapshot snapshot = new Snapshot(in, output, scope);
      final Environment env = scope.newScope();
      try {
         Object result = block.resolve(in, output, env);
         if(returns != null) {
            result = returns.resolve(in, output, env);
         }
         return result;
      } catch(final AbortedException e) {
         throw e.addStackTrace(snapshot, "in %s", name);
      } catch(final ResolveException e) {
         throw e.addStackTrace(snapshot, "in %s", name);
      }
   }

   /**
    * Returns the declaration return value, or null if there is none.
    * @return the return value
    */
   public Expression returns() {
      return returns;
   }

   /**
    * Sets the return value.
    * @param value the new return value
    */
   public void returns(final Expression value) {
      returns = value;
   }

   /**
    * Returns the list of statements.
    * @return the list of statements
    */
   public Iterable<Expression> statements() {
      return block.statements();
   }

   @Override
   public String toString() {
      return "<" + name + ">" + block + " return " + returns;
   }
}
