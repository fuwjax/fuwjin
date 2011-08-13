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
 * An expression representing redirected script I/O.
 */
public class ScriptPipe implements Expression {
   private final Expression sink;
   private final Expression source;

   /**
    * Creates a new instance.
    * @param source the source script
    * @param sink the destination script
    */
   public ScriptPipe(final Expression source, final Expression sink) {
      this.source = source;
      this.sink = sink;
   }

   @Override
   public Object resolve(final Trace trace) throws AbortedException, ResolveException {
      final Trace output = trace.newOutput();
      source.resolve(output);
      final Trace input = trace.newInput(output.toString());
      return sink.resolve(input);
   }

   /**
    * Returns the destination script.
    * @return the destination
    */
   public Expression sink() {
      return sink;
   }

   /**
    * Returns the source script.
    * @return the source
    */
   public Expression source() {
      return source;
   }
}
