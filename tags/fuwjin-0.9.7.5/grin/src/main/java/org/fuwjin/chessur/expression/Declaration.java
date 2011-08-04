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

import java.util.Deque;
import java.util.LinkedList;
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.dinah.Adapter;

/**
 * Represents a Specification declaration.
 */
public class Declaration implements Expression {
   private static class MatchExpression extends Variable {
      private final Deque<SourceStream> buffers = new LinkedList<SourceStream>();

      public MatchExpression() {
         super("match");
      }

      @Override
      public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
            throws AbortedException, ResolveException {
         return buffers.peek().toString();
      }
   }

   private final String name;
   private final Block block;
   private Expression returns;
   private MatchExpression match;

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

   public Expression match() {
      if(match == null) {
         match = new MatchExpression();
      }
      return match;
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
      SourceStream in = input;
      if(match != null) {
         in = input.buffer();
         match.buffers.push(in);
      }
      final Snapshot snapshot = new Snapshot(in, output, scope);
      final Environment env = scope.newScope();
      try {
         block.resolve(in, output, env);
         if(returns != null) {
            return returns.resolve(in, output, env);
         }
         return Adapter.UNSET;
      } catch(final AbortedException e) {
         throw new AbortedException(e, "in %s: %s", name, snapshot);
      } catch(final ResolveException e) {
         throw new ResolveException(e, "in %s: %s", name, snapshot);
      } finally {
         if(match != null) {
            match.buffers.poll();
         }
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
