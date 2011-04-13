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
 * Represents a statement that always succeeds.
 */
public class CouldStatement implements Expression {
   private final Expression statement;

   /**
    * Creates a new instance.
    * @param statement the possible statement
    */
   public CouldStatement(final Expression statement) {
      this.statement = statement;
   }

   @Override
   public String toString() {
      return "could " + statement;
   }

   @Override
   public State transform(final State state) {
      final State result = statement.transform(state);
      if(result.isSuccess()) {
         return result;
      }
      return state;
   }
}
