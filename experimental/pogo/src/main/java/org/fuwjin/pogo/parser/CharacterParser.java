/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.ParsingExpression;
import org.fuwjin.pogo.state.PogoState;

/**
 * Matches any character from the input.
 */
public class CharacterParser implements ParsingExpression {
   @Override
   public boolean equals(final Object obj) {
      return obj instanceof CharacterParser;
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public boolean parse(final PogoState state) {
      return state.advance(0, Integer.MAX_VALUE);
   }

   @Override
   public void resolve(final Grammar grammar, final String namespace) {
      // do nothing
   }

   @Override
   public String toString() {
      return ".";
   }
}
