/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Position;

/**
 * Matches the inner parser zero or one times.
 */
public class OptionalParser extends ParserOperator {
   /**
    * Creates a new instance.
    */
   OptionalParser() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param parser the optional parser
    */
   public OptionalParser(final Parser parser) {
      super(parser);
   }

   @Override
   public Position parse(final Position position) {
      final Position next = parseBuffered(position);
      next.success();
      return next;
   }

   @Override
   public String toString() {
      return parser.toString() + "?";
   }
}
