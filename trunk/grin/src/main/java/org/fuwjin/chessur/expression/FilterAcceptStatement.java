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

import org.fuwjin.dinah.Adapter;
import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;
import org.fuwjin.grin.env.Trace;

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

   /**
    * Returns the filter expression.
    * @return the filter
    */
   public Filter filter() {
      return filter;
   }

   /**
    * Returns true if this statement is negated.
    * @return true if negated, false otherwise
    */
   public boolean isNot() {
      return isNot;
   }

   @Override
   public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
         throws AbortedException, ResolveException {
      if(isNot) {
         if(filter.allow(input.next())) {
            throw trace.fail("Unexpected match: %s", filter);
         }
         input.read();
         return Adapter.UNSET;
      }
      if(filter.allow(input.next())) {
         input.read();
         return Adapter.UNSET;
      }
      throw trace.fail("Did not match filter: %s", filter);
   }

   @Override
   public String toString() {
      return "accept " + (isNot ? "not " : "") + "in " + filter;
   }
}
