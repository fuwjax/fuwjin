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

import org.fuwjin.chessur.stream.CodePointInStream;
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.ObjectOutStream;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.SourceStream;

public class ScriptPipe implements Expression {
   private final Expression sink;
   private final Expression source;

   public ScriptPipe(final Expression source, final Expression sink) {
      this.source = source;
      this.sink = sink;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final SinkStream out = ObjectOutStream.stream();
      source.resolve(input, out, scope);
      final SourceStream in = CodePointInStream.streamOf(out.toString());
      return sink.resolve(in, output, scope);
   }
}
