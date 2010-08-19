/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import org.fuwjin.io.Position;
import org.fuwjin.pogo.Parser;

/**
 * Matches a sequence of various parsers.
 */
public class SequenceParser extends CompositeParser {
   @Override
   protected boolean isLiteral(final Parser parser) {
      return parser instanceof CharacterLiteralParser;
   }

   // intentionally unbuffered
   @Override
   public Position parse(final Position position) {
      Position next = position;
      for(final Parser parser: this) {
         next = parser.parse(next);
         if(!next.isSuccess()) {
            position.fail(next);
            return position;
         }
      }
      return next;
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      for(final Parser parser: this) {
         if(builder.length() > 0) {
            builder.append(' ');
         }
         builder.append(parser);
      }
      return builder.toString();
   }
}
