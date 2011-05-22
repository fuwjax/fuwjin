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
package org.fuwjin.chessur.expression;

import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.dinah.adapter.StandardAdapter;

/**
 * Represents a variable in the current scope.
 */
public class Variable implements Expression {
   /**
    * Represents the next character.
    */
   public static final Variable NEXT = new Variable("next") {
      @Override
      public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
            throws AbortedException, ResolveException {
         return input.next(new Snapshot(input, output, scope)).value();
      }
   };
   private final String name;

   /**
    * Creates a new instance.
    * @param name the variable name
    */
   public Variable(final String name) {
      this.name = name;
   }

   /**
    * Returns the name of the variable.
    * @return the name
    */
   public String name() {
      return name;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final Object value = scope.retrieve(name);
      if(StandardAdapter.isSet(value)) {
         return value;
      }
      throw new ResolveException("variable %s is unset: %s", name, new Snapshot(input, output, scope));
   }

   @Override
   public String toString() {
      return name;
   }
}
