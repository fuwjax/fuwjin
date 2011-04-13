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

/**
 * Represents an unrecoverable failure.
 */
public class AbortStatement implements Expression {
   private final Expression value;

   /**
    * Creates a new instance.
    * @param value the failure message
    */
   public AbortStatement(final Expression value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return "abort " + value;
   }

   @Override
   public State transform(final State state) {
      throw new RuntimeException(String.valueOf(value.transform(state).value()) + " " + state);
   }
}
