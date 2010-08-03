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
   public void parse(final PogoContext context) {
      final Position mark = context.position();
      parser.parse(context);
      if(context.isSuccess()) {
         context.seek(mark);
      }
      context.success(context.isSuccess() ? context.failedCheck("unexpected match") : null);
   }

   @Override
   public String toString() {
      return '!' + parser.toString();
   }
}
