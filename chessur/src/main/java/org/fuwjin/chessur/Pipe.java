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

public class Pipe extends OutStream implements Expression {
   private final String name;
   private final StringBuilder buffer = new StringBuilder();

   public Pipe(final String name) {
      this.name = name;
   }

   @Override
   protected void append(final Object value) {
      buffer.append(value);
   }

   @Override
   public State transform(final State state) {
      final State result = state.value(buffer.toString());
      return result.assign(name);
   }
}
