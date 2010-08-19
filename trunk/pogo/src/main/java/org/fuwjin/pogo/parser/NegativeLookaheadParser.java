/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import org.fuwjin.io.BufferedPosition;
import org.fuwjin.io.Position;
import org.fuwjin.pogo.Parser;

/**
 * A Zero-match parser that fails if the inner match succeeds.
 */
public class NegativeLookaheadParser extends ParserOperator {
   /**
    * Creates a new instance.
    */
   NegativeLookaheadParser() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param parser the unexpected parser
    */
   public NegativeLookaheadParser(final Parser parser) {
      super(parser);
   }

   @Override
   public Position parse(final Position position) {
      final BufferedPosition buffer = position.buffered();
      final Position next = parser.parse(buffer);
      if(next.isSuccess()) {
         buffer.fail("unexpected match", null);
      } else {
         next.success();
      }
      return buffer.flush(buffer);
   }

   @Override
   public String toString() {
      return '!' + parser.toString();
   }
}
