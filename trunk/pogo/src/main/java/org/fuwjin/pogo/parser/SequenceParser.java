/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import org.fuwjin.io.PogoContext;
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

   @Override
   public void parse(final PogoContext context) {
      final Position mark = context.position();
      for(final Parser parser: this) {
         parser.parse(context);
         if(!context.isSuccess()) {
            context.seek(mark);
            break;
         }
      }
   }
}
