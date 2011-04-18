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

public class ScriptPipe extends Transformer {
   private final Transformer sink;
   private final Transformer source;

   public ScriptPipe(final Transformer source, final Transformer sink) {
      this.source = source;
      this.sink = sink;
   }

   @Override
   public State transform(final State state) {
      final OutStream out = OutStream.stream();
      final State output = state.redirectOutput(out);
      if(!output.isSuccess()) {
         return state.failure(output, "Could not redirect output");
      }
      final State pipe = source.transform(output);
      if(!pipe.isSuccess()) {
         return state.failure(pipe, "Could not transform source");
      }
      final State first = pipe.restoreIo(pipe, state);
      final State input = first.redirectInput(InStream.streamOf(out.toString()));
      final State result = sink.transform(input);
      if(!result.isSuccess()) {
         return state.failure(result, "Could not transform sink");
      }
      return result.restoreIo(first, result);
   }
}
