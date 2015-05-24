/**
 * 
 */
package org.fuwjin.test;

import static org.fuwjin.pogo.PogoGrammar.pogoParseGrammar;
import static org.fuwjin.pogo.PogoUtils.readGrammar;
import static org.fuwjin.pogo.PogoUtils.writeGrammar;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoCodeGenerator;
import org.fuwjin.pogo.PredefinedGrammar;
import org.junit.Test;

/**
 * Tests standard Pogo parsing.
 */
public class PogoWriterTest {
   private static final String GRAMMAR = " grammar"; //$NON-NLS-1$
   private static final String COMMENT = "# "; //$NON-NLS-1$
   private static final String PACKAGE = "org.fuwjin.generated."; //$NON-NLS-1$

   /**
    * Generates the hardcoded parsers for each of the predefined grammars.
    * @throws Exception if there was a failure
    */
   @Test
   public void outputGrammars() throws Exception {
      for(final PredefinedGrammar grammar: PredefinedGrammar.values()) {
         System.out.println(COMMENT + grammar + GRAMMAR);
         System.out.println(writeGrammar(grammar.grammar()));
      }
      for(final PredefinedGrammar grammar: PredefinedGrammar.values()) {
         System.out.println(new PogoCodeGenerator(PACKAGE + grammar.toString(), grammar.grammar()).toCode());
      }
   }

   /**
    * Tests that the Pogo parser can parse itself back into an equivalent
    * parser.
    * @throws Exception if it fails
    */
   @Test
   public void testWriter() throws Exception {
      final String grammar = writeGrammar(pogoParseGrammar());
      final Grammar peg = readGrammar(new StringReader(grammar));
      final String written = writeGrammar(peg);
      assertThat(written, is(grammar));
   }
}
