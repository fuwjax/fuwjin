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
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.dinah.Adapter;

/**
 * Publishes a value to an outstream.
 */
public class LogStatement implements Expression {
   private final Expression value;

   /**
    * Creates a new instance.
    * @param value the value to publish
    */
   public LogStatement(final Expression value) {
      this.value = value;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final Object result = value.resolve(input, output, scope);
      output.log(result);
      return Adapter.UNSET;
   }

   @Override
   public String toString() {
      return "log " + value;
   }

   /**
    * Returns the published value.
    * @return the value
    */
   public Expression value() {
      return value;
   }
}
