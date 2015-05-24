/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.pogo.parser;

import org.fuwjin.pogo.ParsingExpression;
import org.fuwjin.pogo.state.PogoPosition;
import org.fuwjin.pogo.state.PogoState;

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
   public NegativeLookaheadParser(final ParsingExpression parser) {
      super(parser);
   }

   @Override
   public boolean parse(final PogoState state) {
      final PogoPosition mark = state.mark();
      try {
         if(parser.parse(state)) {
            mark.reset();
            state.fail("unexpected match", null);
            return false;
         }
         return true;
      } finally {
         mark.release();
      }
   }

   @Override
   public String toString() {
      return '!' + parser.toString();
   }
}
