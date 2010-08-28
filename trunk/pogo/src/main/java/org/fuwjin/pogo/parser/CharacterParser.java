/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Position;
import org.fuwjin.pogo.Rule;

/**
 * Matches any character from the input.
 */
public class CharacterParser implements Parser {
   @Override
   public boolean equals(final Object obj) {
      return obj instanceof CharacterParser;
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public Position parse(final Position position) {
      return position.advance(0, Integer.MAX_VALUE);
   }

   @Override
   public void resolve(final Grammar grammar, final Rule parent) {
      // do nothing
   }

   @Override
   public String toString() {
      return ".";
   }
}
