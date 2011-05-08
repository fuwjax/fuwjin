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
package org.fuwjin.chessur;

import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.util.Adapter;

/**
 * Publishes a value to an outstream.
 */
public class PublishStatement implements Expression {
   private final Expression value;

   /**
    * Creates a new instance.
    * @param value the value to publish
    */
   public PublishStatement(final Expression value) {
      this.value = value;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final Object result = value.resolve(input, output, scope);
      output.append(result);
      return Adapter.unset();
   }

   @Override
   public String toString() {
      return "publish " + value;
   }
}
