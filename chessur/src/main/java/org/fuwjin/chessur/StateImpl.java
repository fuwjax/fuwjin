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

import org.fuwjin.util.Adapter;

/**
 * Represents an immutable state.
 */
public class StateImpl implements State {
   private final InStream.Position input;
   private final OutStream.Position output;
   private final Scope scope;
   private final Object value;

   /**
    * Creates a new instance.
    * @param input the input position
    * @param output the output position
    * @param scope the scope
    * @param value the bound object
    */
   public StateImpl(final InStream.Position input, final OutStream.Position output, final Scope scope,
         final Object value) {
      this.input = input;
      this.output = output;
      this.scope = scope;
      this.value = value;
   }

   @Override
   public State accept() {
      final int ch = input.codePoint();
      if(ch == InStream.EOF) {
         return failure("cannot accept past EOF");
      }
      return new StateImpl(input.next(), output, scope, ch);
   }

   @Override
   public State assign(final String name) {
      if(!Adapter.isSet(value)) {
         return failure("cannot assign an unset value");
      }
      return new StateImpl(input, output, scope.assign(name, value), value);
   }

   @Override
   public State assign(final String name, final Object val) {
      return new StateImpl(input, output, scope.assign(name, val), value);
   }

   @Override
   public int current() {
      return input.codePoint();
   }

   @Override
   public State failure(final State cause, final String message, final Object... args) {
      return new FailureState(this, cause, message, args);
   }

   @Override
   public State failure(final String message, final Object... args) {
      return new FailureState(this, message, args);
   }

   @Override
   public boolean isSuccess() {
      return true;
   }

   @Override
   public State mark() {
      return new StateImpl(input, output, scope.mark(input), value);
   }

   @Override
   public State publish() {
      if(!Adapter.isSet(value)) {
         return failure("cannot publish unset value");
      }
      return new StateImpl(input, output.append(value), scope, null);
   }

   @Override
   public State redirectInput(final InStream newInput) {
      return new StateImpl(newInput.start(), output, scope, value);
   }

   @Override
   public State redirectOutput(final OutStream newOutput) {
      return new StateImpl(input, newOutput.start(), scope, value);
   }

   @Override
   public State restoreIo(final State in, final State out) {
      return new StateImpl(((StateImpl)in).input, ((StateImpl)out).output, scope, value);
   }

   @Override
   public State restoreScope(final State state) {
      return new StateImpl(input, output, ((StateImpl)state).scope, value);
   }

   @Override
   public State retrieve(final String name) {
      final Object v = scope.retrieve(name);
      if(!Adapter.isSet(v)) {
         return failure("variable %s is unset", name);
      }
      return value(v);
   }

   @Override
   public State substring() {
      return new StateImpl(input, output, scope, input.substring(scope.mark()));
   }

   @Override
   public String toString() {
      return input + " -> " + output;
   }

   @Override
   public Object value() {
      return value;
   }

   @Override
   public State value(final Object newValue) {
      return new StateImpl(input, output, scope, newValue);
   }
}
