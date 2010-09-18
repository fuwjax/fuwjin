/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.state.PogoPosition;
import org.fuwjin.pogo.state.PogoState;

/**
 * Matches a set of parsers in order against the input. As soon as any inner
 * parser matches, the whole option matches.
 */
public class OptionParser extends CompositeParser {
   @Override
   protected boolean isLiteral(final Parser parser) {
      return parser instanceof CharacterLiteralParser || parser instanceof CharacterRangeParser;
   }

   @Override
   public boolean parse(final PogoState state) {
      final PogoPosition mark = state.mark();
      try {
         for(final Parser parser: this) {
            if(parser.parse(state)) {
               return true;
            }
            mark.reset();
         }
         return false;
      } finally {
         mark.release();
      }
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      for(final Parser parser: this) {
         if(builder.length() > 0) {
            builder.append('/');
         }
         builder.append(parser);
      }
      return builder.toString();
   }
}
