/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static org.fuwjin.pogo.CodePointStreamFactory.streamOf;
import static org.fuwjin.pogo.PogoGrammar.readGrammar;
import static org.fuwjin.pogo.PogoGrammar.staticPogoGrammar;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjin.pogo.Grammar;
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
         System.out.println(grammar.grammar().toPogo());
      }
      for(final PredefinedGrammar grammar: PredefinedGrammar.values()) {
         System.out.println(grammar.grammar().toCode(PACKAGE + grammar.toString()));
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
      final Grammar peg = readGrammar(streamOf(grammar));
      final String written = peg.toPogo();
      assertThat(written, is(grammar));
   }
}
