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

   @Override
   public String toString() {
      return "accept " + (isNot ? "not " : "") + "in " + filter;
   }

   @Override
   public State transform(final State state) {
      if(state.current() == InStream.EOF) {
         return state.failure("unexpected EOF");
      }
      if(isNot) {
         if(filter.allow(state.current())) {
            return state.failure("Unexpected match: %s", filter);
         }
         return state.accept();
      }
      if(filter.allow(state.current())) {
         return state.accept();
      }
      return state.failure("Did not match filter: %s", filter);
   }
}
