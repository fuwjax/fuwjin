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
 * Matches the inner parser zero or more times.
 */
public class OptionalSeriesParser extends ParserOperator {
   /**
    * Creates a new instance.
    */
   OptionalSeriesParser() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param parser the optionally repeating parser
    */
   public OptionalSeriesParser(final Parser parser) {
      super(parser);
   }

   @Override
   public Position parse(final Position position) {
      Position next = position;
      do {
         next = parseBuffered(next);
      } while(next.isSuccess());
      next.success();
      return next;
   }

   @Override
   public String toString() {
      return parser.toString() + "*";
   }
}
