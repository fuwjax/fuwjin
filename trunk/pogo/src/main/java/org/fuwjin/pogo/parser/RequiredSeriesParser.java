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
 * Matches the inner parser one or more times.
 */
public class RequiredSeriesParser extends ParserOperator {
   /**
    * Creates a new instance.
    * @param parser the required parser
    */
   public RequiredSeriesParser(final Parser parser) {
      super(parser);
   }

   /**
    * Creates a new instance.
    */
   RequiredSeriesParser() {
      // for reflection
   }

   @Override
   public void parse(final PogoContext context) {
      final int mark = context.position();
      do {
         parser.parse(context);
      } while(context.isSuccess());
      if(mark != context.position()) {
         context.success(true, null);
      }
   }
}
