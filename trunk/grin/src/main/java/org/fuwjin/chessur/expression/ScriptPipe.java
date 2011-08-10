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

import java.io.StringReader;
import java.io.StringWriter;
import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;
import org.fuwjin.grin.env.StandardEnv;
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
   public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
         throws AbortedException, ResolveException {
      final StringWriter writer = new StringWriter();
      final Sink out = StandardEnv.publishTo(writer);
      source.resolve(input, out, scope, trace.newOutput(out));
      final Source in = StandardEnv.acceptFrom(new StringReader(writer.toString()));
      return sink.resolve(in, output, scope, trace.newInput(in));
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
