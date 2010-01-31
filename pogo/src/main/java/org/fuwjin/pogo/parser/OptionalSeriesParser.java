package org.fuwjin.pogo.parser;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.Parser;

/**
 * Matches the inner parser zero or more times.
 */
public class OptionalSeriesParser extends ParserOperator {
   /**
    * Creates a new instance.
    * @param parser the optionally repeating parser
    */
   public OptionalSeriesParser(final Parser parser) {
      super(parser);
   }

   /**
    * Creates a new instance.
    */
   OptionalSeriesParser() {
      // for reflection
   }

   @Override
   public void parse(final PogoContext context) {
      do {
         parser.parse(context);
      } while(context.isSuccess());
      context.success(true, null);
   }
}
