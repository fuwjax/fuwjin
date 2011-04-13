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
 * Represents an assignment.
 */
public class Assignment implements Expression {
   private final String name;
   private final Expression value;

   /**
    * Creates a new instance.
    * @param name the variable name
    * @param value the new value
    */
   public Assignment(final String name, final Expression value) {
      this.name = name;
      this.value = value;
   }

   @Override
   public String toString() {
      return name + " = " + value;
   }

   @Override
   public State transform(final State state) {
      final State result = value.transform(state);
      if(!result.isSuccess()) {
         return state.failure(result, "could not assign to %s", name);
      }
      return result.assign(name);
   }
}
