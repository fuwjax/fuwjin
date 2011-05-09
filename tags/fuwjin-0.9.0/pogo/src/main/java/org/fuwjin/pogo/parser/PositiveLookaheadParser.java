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
 * A zero-match parser that succeeds if the inner parser succeeds.
 */
public class PositiveLookaheadParser extends ParserOperator {
   /**
    * Creates a new instance.
    */
   PositiveLookaheadParser() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param parser the expected parser
    */
   public PositiveLookaheadParser(final Parser parser) {
      super(parser);
   }

   @Override
   public Position parse(final Position position) {
      final BufferedPosition buffer = position.buffered();
      final Position next = parser.parse(buffer);
      if(!next.isSuccess()) {
         buffer.fail("expected match", null);
      }
      return buffer.flush(buffer);
   }

   @Override
   public String toString() {
      return '&' + parser.toString();
   }
}