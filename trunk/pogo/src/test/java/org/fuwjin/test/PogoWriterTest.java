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
package org.fuwjin.test;

import static org.fuwjin.pogo.CodePointStreamFactory.streamOf;
import static org.fuwjin.pogo.PogoGrammar.staticPogoGrammar;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoException;
import org.fuwjin.pogo.PredefinedGrammar;
import org.junit.Test;

/**
 * Tests standard Pogo parsing.
 */
public class PogoWriterTest {
   private static final String GRAMMAR = " grammar"; //$NON-NLS-1$
   private static final String COMMENT = "# "; //$NON-NLS-1$
   private static final String PACKAGE = "org.fuwjin.generated."; //$NON-NLS-1$
   private static boolean toggle = false;

   /**
    * Support function for Issue 1.
    * @return true then false
    */
   public static boolean toggle() {
      return toggle = !toggle;
   }

   /**
    * Generates the hardcoded parsers for each of the predefined grammars.
    * @throws Exception if there was a failure
    */
   @Test
   public void outputGrammars() throws Exception {
      new File("target/generated/" + PACKAGE.replace(".", "/")).mkdirs();
      for(final PredefinedGrammar grammar: PredefinedGrammar.values()) {
         final Writer writer = new FileWriter("target/generated/" + grammar + ".pogo");
         writer.append(COMMENT + grammar + GRAMMAR + '\n');
         writer.append(grammar.grammar().toPogo());
         writer.close();
      }
      for(final PredefinedGrammar grammar: PredefinedGrammar.values()) {
         final Writer writer = new FileWriter("target/generated/" + PACKAGE.replace(".", "/") + grammar + ".java");
         writer.append(grammar.grammar().toCode(PACKAGE + grammar.toString()));
         writer.close();
      }
   }

   /**
    * Test for Issue 1.
    * @throws Exception if it fails
    */
   @Test
   public void testSimpleWriteRewind() throws Exception {
      final Grammar grammar = Grammar
            .readGrammar(streamOf("Rule <- ('test' Sub)* Sub = org.fuwjin.test.PogoWriterTest~toggle <- ''"));
      final String out = grammar.toString(null);
      assertThat(out, is("test"));
   }

   /**
    * Test for Issue 2.
    * @throws Exception if it fails
    */
   @Test
   public void testWriteClassShouldFail() throws Exception {
      final Grammar grammar = Grammar.readGrammar(streamOf("Rule <- [a-z]"));
      try {
         grammar.toString("test");
         fail("Serialization does not support character classes");
      } catch(final PogoException e) {
         // pass
      }
   }

   /**
    * Tests that the Pogo parser can parse itself back into an equivalent
    * parser.
    * @throws Exception if it fails
    */
   @Test
   public void testWriter() throws Exception {
      final String grammar = staticPogoGrammar().toPogo();
      final Grammar peg = Grammar.readGrammar(streamOf(grammar));
      final String written = peg.toPogo();
      assertThat(written, is(grammar));
   }
}
