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

/**
 * Accepts based on a filter.
 */
public class FilterAcceptStatement implements Expression {
   private final boolean isNot;
   private final Filter filter;

   /**
    * Creates a new instance.
    * @param isNot if the result should be reversed
    * @param filter the filter to apply
    */
   public FilterAcceptStatement(final boolean isNot, final Filter filter) {
      this.isNot = isNot;
      this.filter = filter;
   }

   public Filter filter() {
      return filter;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws ResolveException {
      final Snapshot snapshot = new Snapshot(input, output, scope);
      if(isNot) {
         if(filter.allow((Integer)input.next(snapshot).value())) {
            throw new ResolveException("Unexpected match: %s: %s", filter, snapshot);
         }
         return input.read(snapshot).value();
      }
      if(filter.allow((Integer)input.next(snapshot).value())) {
         return input.read(snapshot).value();
      }
      throw new ResolveException("Did not match filter: %s: %s", filter, snapshot);
   }

   @Override
   public String toString() {
      return "accept " + (isNot ? "not " : "") + "in " + filter;
   }
}
