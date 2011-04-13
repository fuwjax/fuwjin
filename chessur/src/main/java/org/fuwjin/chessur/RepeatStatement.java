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
 * Represents a statement that repeats one or more times.
 */
public class RepeatStatement implements Expression {
   private final Expression statement;

   /**
    * Creates a new instance.
    * @param statement the repeated statement
    */
   public RepeatStatement(final Expression statement) {
      this.statement = statement;
   }

   @Override
   public String toString() {
      return "repeat " + statement;
   }

   @Override
   public State transform(final State state) {
      State result = statement.transform(state);
      if(!result.isSuccess()) {
         return result;
      }
      State last = state;
      while(result.isSuccess() && result != last) {
         last = result;
         result = statement.transform(result);
      }
      return last;
   }
}
