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

import org.fuwjin.chessur.stream.CodePointInStream;
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.SourceStream;

public class ScriptInput implements Expression {
   private final Expression spec;
   private final Expression value;

   public ScriptInput(final Expression spec, final Expression value) {
      this.spec = spec;
      this.value = value;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final Object val = value.resolve(input, output, scope);
      final SourceStream in = CodePointInStream.streamOf(String.valueOf(val));
      return spec.resolve(in, output, scope);
   }
}
