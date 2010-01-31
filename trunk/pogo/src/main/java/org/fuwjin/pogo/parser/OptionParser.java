package org.fuwjin.pogo.parser;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.Parser;

/**
 * Matches a set of parsers in order against the input. As soon as any inner
 * parser matches, the whole option matches.
 */
public class OptionParser extends CompositeParser {
   @Override
   public void parse(final PogoContext context) {
      for(final Parser parser: this) {
         parser.parse(context);
         if(context.isSuccess()) {
            break;
         }
      }
      // context is failure if loop never broke
   }

   @Override
   protected boolean isLiteral(final Parser parser) {
      return parser instanceof CharacterLiteralParser || parser instanceof CharacterRangeParser;
   }
}
