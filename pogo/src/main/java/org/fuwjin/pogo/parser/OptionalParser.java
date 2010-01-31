package org.fuwjin.pogo.parser;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.Parser;

/**
 * Matches the inner parser zero or one times.
 */
public class OptionalParser extends ParserOperator {
   /**
    * Creates a new instance.
    * @param parser the optional parser
    */
   public OptionalParser(final Parser parser) {
      super(parser);
   }

   /**
    * Creates a new instance.
    */
   OptionalParser() {
      // for reflection
   }

   @Override
   public void parse(final PogoContext context) {
      parser.parse(context);
      context.success(true, null);
   }
}
