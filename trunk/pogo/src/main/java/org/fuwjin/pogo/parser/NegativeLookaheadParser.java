/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.Parser;

/**
 * A Zero-match parser that fails if the inner match succeeds.
 */
public class NegativeLookaheadParser extends ParserOperator {
   private static final String UNEXPECTED_MATCH = "Unexpected match"; //$NON-NLS-1$

   /**
    * Creates a new instance.
    * @param parser the unexpected parser
    */
   public NegativeLookaheadParser(final Parser parser) {
      super(parser);
   }

   /**
    * Creates a new instance.
    */
   NegativeLookaheadParser() {
      // for reflection
   }

   @Override
   public void parse(final PogoContext context) {
      final int mark = context.position();
      parser.parse(context);
      if(context.isSuccess()) {
         context.seek(mark);
      }
      context.success(!context.isSuccess(), UNEXPECTED_MATCH);
   }
}