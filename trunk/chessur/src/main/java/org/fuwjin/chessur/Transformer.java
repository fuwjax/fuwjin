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

import java.util.Collections;
import java.util.Map;

import org.fuwjin.util.Adapter;

/**
 * Base utility class for all things transformative.
 */
public abstract class Transformer implements Expression {
   /**
    * Parses the input stream.
    * @param input the input stream
    * @return the result
    */
   public Object transform(final InStream input) {
      return transform(input, OutStream.NONE, Collections.<String, Object> emptyMap());
   }

   /**
    * Parses the input stream.
    * @param input the input stream
    * @param scope the initial scope
    * @return the result
    */
   public Object transform(final InStream input, final Map<String, ? extends Object> scope) {
      return transform(input, OutStream.NONE, scope);
   }

   /**
    * Transforms the input, output, and scope into the result.
    * @param input the input stream
    * @param output the output stream
    * @param scope the scope
    * @return the result
    */
   public Object transform(final InStream input, final OutStream output, final Map<String, ? extends Object> scope) {
      final State state = transform(new StateImpl(input.start(), output.start(), new Scope(scope), Adapter.unset()));
      return state.value();
   }
}
