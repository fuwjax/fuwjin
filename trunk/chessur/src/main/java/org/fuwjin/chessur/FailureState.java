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
 * Represents a failure during transformation.
 */
public class FailureState implements State {
   private final State parent;
   private final String message;
   private final Object[] args;
   private final State cause;

   public FailureState(final State lastGood, final State cause, final String message, final Object... args) {
      parent = lastGood;
      this.cause = cause;
      this.message = message;
      this.args = args;
   }

   /**
    * Creates a new instance.
    * @param parent the parent state, not necessarily a failure.
    * @param message the failure message pattern
    * @param args the message args
    */
   public FailureState(final State lastGood, final String message, final Object... args) {
      parent = lastGood;
      cause = null;
      this.message = message;
      this.args = args;
   }

   @Override
   public State accept() {
      throw exception();
   }

   @Override
   public State assign(final String name) {
      throw exception();
   }

   @Override
   public State assign(final String name, final Object value) {
      throw exception();
   }

   @Override
   public int current() {
      throw exception();
   }

   @Override
   public State failure(final State result, final String msg, final Object... arguments) {
      throw exception();
   }

   @Override
   public State failure(final String msg, final Object... arguments) {
      throw exception();
   }

   @Override
   public boolean isSuccess() {
      return false;
   }

   @Override
   public State mark() {
      throw exception();
   }

   @Override
   public State publish() {
      throw exception();
   }

   @Override
   public State redirectInput(final InStream newInput) {
      throw exception();
   }

   @Override
   public State redirectOutput(final OutStream newOutput) {
      throw exception();
   }

   @Override
   public State restoreIo(final State in, final State out) {
      throw exception();
   }

   @Override
   public State restoreScope(final State state) {
      throw exception();
   }

   @Override
   public State retrieve(final String name) {
      throw exception();
   }

   @Override
   public State substring() {
      throw exception();
   }

   @Override
   public String toString() {
      if(cause == null) {
         return String.format(message, args) + ": " + parent;
      }
      return cause + "\n" + String.format(message, args) + ": " + parent;
   }

   @Override
   public Object value() {
      throw exception();
   }

   @Override
   public State value(final Object value) {
      throw exception();
   }

   private RuntimeException exception() {
      return new RuntimeException(toString());
   }
}
