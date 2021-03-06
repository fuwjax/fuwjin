/**
 * 
 */
package org.fuwjin.test;

import static org.fuwjin.pogo.PogoGrammar.pogoParseGrammar;
import static org.fuwjin.pogo.PogoUtils.open;
import static org.fuwjin.pogo.PogoUtils.readGrammar;
import static org.fuwjin.pogo.PogoUtils.writeGrammar;
import static org.fuwjin.pogo.PredefinedGrammar.PogoParse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import org.fuwjin.pogo.Grammar;
import org.junit.Test;

/**
 * Tests standard Pogo parsing.
 */
public class PogoTest {
   private static final String POGO_GRAMMAR = "pogoParse.pogo"; //$NON-NLS-1$

   /**
    * Tests that the parsed parser is functionally equivalent to the hardcoded
    * parser.
    * @throws Exception if the test fails
    */
   @Test
   public void testParsedParser() throws Exception {
      final Grammar parsed = (Grammar)PogoParse.get().parse(open(POGO_GRAMMAR));
      final Grammar orig = readGrammar(POGO_GRAMMAR);
      assertThat(orig, is(parsed));
   }

   /**
    * Tests that the Pogo parser can parse itself back into an equivalent
    * parser.
    * @throws Exception if it fails
    */
   @Test
   public void testParser() throws Exception {
      final String grammar = writeGrammar(pogoParseGrammar());
      final Grammar peg = readGrammar(new StringReader(grammar));
      final Grammar peg2 = (Grammar)peg.parse(new StringReader(grammar));
      assertThat(peg, is(peg2));
   }
}
