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
 * Represents the transform state.
 */
public interface State {
   /**
    * Accepts the next code point in the input and sets the value of the state
    * to the consumed character.
    * @return the new state
    */
   public abstract State accept();

   /**
    * Assigns the current value of this state to the scope variable name.
    * @param name the name of the scope variable
    * @return the new state
    */
   public abstract State assign(final String name);

   public abstract State assign(String name, Object value);

   /**
    * Returns the current code point in the input stream.
    * @return the current code point
    */
   public abstract int current();

   /**
    * Generates a new failure.
    * @param cause the cause of this failure, expected to also be a failure
    * @param message the failure message pattern
    * @param args the arguments to the pattern
    * @return the new failure state.
    */
   public abstract State failure(final State cause, final String message, final Object... args);

   /**
    * Generates a new failure.
    * @param message the failure message pattern
    * @param args the arguments to the pattern
    * @return the new failure state.
    */
   public abstract State failure(final String message, final Object... args);

   /**
    * Returns true if this is still a viable state.
    * @return true if this is a success, false if a failure
    */
   public abstract boolean isSuccess();

   /**
    * Returns a state with the current input position marked in the scope.
    * @return the marked state
    */
   public abstract State mark();

   /**
    * Publishes the current value to the output stream.
    * @return the new state
    */
   public abstract State publish();

   State redirectInput(InStream newInput);

   public abstract State redirectOutput(OutStream newOutput);

   public abstract State restoreIo(State state);

   /**
    * Restores the scope from an earlier state.
    * @param state the earlier state
    * @return the new state
    */
   public abstract State restoreScope(final State state);

   /**
    * Retrieves the value of a named variable.
    * @param name the name of the variable
    * @return the new state
    */
   public abstract State retrieve(final String name);

   /**
    * Returns the substring from the current scope's marked position to the
    * current state's position.
    * @return the substring
    */
   public abstract State substring();

   /**
    * Returns the object bound to this state.
    * @return the bound object
    */
   public abstract Object value();

   /**
    * Sets the bound object on the state.
    * @param value the new object
    * @return the new state
    */
   public abstract State value(final Object value);
}
