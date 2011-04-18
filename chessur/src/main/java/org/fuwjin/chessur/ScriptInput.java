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

public class ScriptInput extends Transformer {
   private final Transformer spec;
   private final Expression value;

   public ScriptInput(final Transformer spec, final Expression value) {
      this.spec = spec;
      this.value = value;
   }

   @Override
   public State transform(final State state) {
      final State valState = value.transform(state);
      if(!valState.isSuccess()) {
         return state.failure(valState, "Could not evaluate value");
      }
      final InStream in = InStream.streamOf(String.valueOf(valState.value()));
      final State input = valState.redirectInput(in);
      if(!input.isSuccess()) {
         return state.failure(input, "Could not redirect input");
      }
      final State result = spec.transform(input);
      if(!result.isSuccess()) {
         return state.failure(result, "Could not transform state");
      }
      final State restore = result.restoreIo(valState, result);
      if(!restore.isSuccess()) {
         return state.failure(restore, "Could not restore input");
      }
      return restore;
   }
}
