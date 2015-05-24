package org.fuwjin.pogo.parser;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.Parser;

/**
 * Matches a sequence of various parsers.
 */
public class SequenceParser extends CompositeParser {
   @Override
   public void parse(final PogoContext context) {
      final int mark = context.position();
      for(final Parser parser: this) {
         parser.parse(context);
         if(!context.isSuccess()) {
            context.seek(mark);
            break;
         }
      }
   }

   @Override
   protected boolean isLiteral(final Parser parser) {
      return parser instanceof CharacterLiteralParser;
   }
}
