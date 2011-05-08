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

import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.util.Adapter;

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
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final Snapshot snapshot = new Snapshot(input, output, scope);
      try {
         final Object result = value.resolve(input, output, scope);
         scope.assign(name, result);
         return Adapter.unset();
      } catch(final ResolveException e) {
         throw e.addStackTrace(snapshot, "could not assign to %s", name);
      }
   }

   @Override
   public String toString() {
      return name + " = " + value;
   }
}
