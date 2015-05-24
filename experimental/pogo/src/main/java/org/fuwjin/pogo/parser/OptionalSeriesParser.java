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
   public OptionalSeriesParser(final ParsingExpression parser) {
      super(parser);
   }

   @Override
   public boolean parse(final PogoState state) {
      PogoPosition mark;
      do {
         mark = state.mark();
         parser.parse(state);
         mark.release();
      } while(state.isAfter(mark));
      return true;
   }

   @Override
   public String toString() {
      return parser.toString() + "*";
   }
}
