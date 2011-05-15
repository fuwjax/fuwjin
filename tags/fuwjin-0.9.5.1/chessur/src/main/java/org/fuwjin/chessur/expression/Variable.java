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

import org.fuwjin.chessur.stream.CodePointInStream;
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.util.Adapter;

/**
 * Represents a variable in the current scope.
 */
public class Variable implements Expression {
   /**
    * Represents a match within a specification.
    */
   public static final Variable MATCH = new Variable("match") {
      @Override
      public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
            throws AbortedException, ResolveException {
         return CodePointInStream.stringOf(input.buffer(new Snapshot(input, output, scope)));
      }
   };
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

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final Object value = scope.retrieve(name);
      if(Adapter.isSet(value)) {
         return value;
      }
      throw new ResolveException("variable %s is unset: %s", name, new Snapshot(input, output, scope));
   }

   @Override
   public String toString() {
      return name;
   }
}
