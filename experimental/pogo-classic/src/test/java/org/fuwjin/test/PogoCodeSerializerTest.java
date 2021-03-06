/**
 * 
 */
package org.fuwjin.test;

import static org.fuwjin.pogo.PogoUtils.readGrammar;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.fuwjin.complier.RuntimeClassLoader;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoCodeGenerator;
import org.junit.Test;

/**
 * Tests csv parsing.
 */
public class PogoCodeSerializerTest {
   private static final String QUALIFIED_NAME = "org.fuwjin.generated.PogoGrammar"; //$NON-NLS-1$
   private static final String TEST_POGO = "pogoParse.pogo"; //$NON-NLS-1$

   /**
    * Tests that a semi-interesting csv can be digested.
    * @throws Exception if there is a failure
    */
   @Test
   public void testCodeGeneration() throws Exception {
      final Grammar pogo = readGrammar(TEST_POGO);
      final String code = new PogoCodeGenerator(QUALIFIED_NAME, pogo).toCode();
      final RuntimeClassLoader loader = new RuntimeClassLoader();
      assertTrue(loader.compile(QUALIFIED_NAME, code));
      final Grammar compiled = (Grammar)loader.loadClass(QUALIFIED_NAME).newInstance();
      assertThat(new PogoCodeGenerator(QUALIFIED_NAME, compiled).toCode(), is(code));
   }
}
