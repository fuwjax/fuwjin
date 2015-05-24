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

import static org.fuwjin.pogo.CodePointStreamFactory.streamBytes;
import static org.fuwjin.pogo.CodePointStreamFactory.streamOf;
import static org.fuwjin.pogo.PogoGrammar.staticPogoGrammar;
import static org.fuwjin.pogo.PredefinedGrammar.PogoParse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
      final Grammar parsed = (Grammar)PogoParse.grammar().parse(streamBytes(POGO_GRAMMAR));
      final Grammar orig = Grammar.readGrammar(streamBytes(POGO_GRAMMAR));
      assertThat(orig, is(parsed));
   }

   /**
    * Tests that the Pogo parser can parse itself back into an equivalent
    * parser.
    * @throws Exception if it fails
    */
   @Test
   public void testParser() throws Exception {
      final String grammar = staticPogoGrammar().toPogo();
      final Grammar peg = Grammar.readGrammar(streamOf(grammar));
      final Grammar peg2 = (Grammar)peg.parse(streamOf(grammar));
      assertThat(peg, is(peg2));
   }
}
