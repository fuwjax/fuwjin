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

public class ScriptOutput extends Transformer {
   private final Transformer spec;
   private final String name;

   public ScriptOutput(final Transformer spec, final String name) {
      this.spec = spec;
      this.name = name;
   }

   @Override
   public State transform(final State state) {
      final OutStream out = OutStream.stream();
      final State output = state.redirectOutput(out);
      if(!output.isSuccess()) {
         return state.failure(output, "Could not redirect output");
      }
      final State result = spec.transform(output);
      if(!result.isSuccess()) {
         return state.failure(result, "Could not transform state");
      }
      final State restore = result.restoreIo(result, state);
      if(!restore.isSuccess()) {
         return state.failure(restore, "Could not restore output");
      }
      final State stored = restore.assign(name, out.toString());
      if(!stored.isSuccess()) {
         return state.failure(stored, "Could not assign output");
      }
      return stored;
   }
}
