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

import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;
import org.fuwjin.grin.env.Trace;

/**
 * Represents a Specification declaration.
 */
public class Declaration implements Expression {
   private final String name;
   private final Block block;
   private Expression returns;
   private Expression exec;
   private Expression match;

   /**
    * Creates a new instance.
    * @param name the specification name
    */
   public Declaration(final String name) {
      this.name = name;
      block = new Block();
      exec = block;
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
         match = new Variable("match") {
            @Override
            public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
                  throws AbortedException, ResolveException {
               return super.resolve(input, output, scope, trace).toString();
            }
         };
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
   public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
         throws AbortedException, ResolveException {
      if(match == null) {
         return trace.resolve(name, exec);
      }
      return trace.resolveMatch(name, exec);
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
      exec = new Expression() {
         @Override
         public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
               throws AbortedException, ResolveException {
            block.resolve(input, output, scope, trace);
            return returns.resolve(input, output, scope, trace);
         }
      };
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
