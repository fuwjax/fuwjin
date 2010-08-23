/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import org.fuwjin.pogo.BufferedPosition;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Position;

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
   public Position parse(final Position position) {
      final BufferedPosition buffer = position.buffered();
      for(final Parser parser: this) {
         buffer.success();
         final Position next = parser.parse(buffer);
         if(next.isSuccess()) {
            return buffer.flush(next);
         }
      }
      return buffer.flush(buffer);
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
