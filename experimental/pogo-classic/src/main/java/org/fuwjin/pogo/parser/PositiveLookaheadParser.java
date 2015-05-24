package org.fuwjin.pogo.parser;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.Parser;

/**
 * A zero-match parser that succeeds if the inner parser succeeds.
 */
public class PositiveLookaheadParser extends ParserOperator {
   /**
    * Creates a new instance.
    * @param parser the expected parser
    */
   public PositiveLookaheadParser(final Parser parser) {
      super(parser);
   }

   /**
    * Creates a new instance.
    */
   PositiveLookaheadParser() {
      // for reflection
   }

   @Override
   public void parse(final PogoContext context) {
      final int mark = context.position();
      parser.parse(context);
      if(context.isSuccess()) {
         context.seek(mark);
      }
   }
}
