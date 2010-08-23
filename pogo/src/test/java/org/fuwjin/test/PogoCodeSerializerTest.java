/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static org.fuwjin.pogo.CodePointStreamFactory.stream;
import static org.fuwjin.pogo.PogoGrammar.readGrammar;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.fuwjin.complier.RuntimeClassLoader;
import org.fuwjin.pogo.Grammar;
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
      final Grammar pogo = readGrammar(stream(TEST_POGO));
      final String code = pogo.toCode(QUALIFIED_NAME);
      final RuntimeClassLoader loader = new RuntimeClassLoader();
      assertTrue(loader.compile(QUALIFIED_NAME, code));
      final Grammar compiled = (Grammar)loader.loadClass(QUALIFIED_NAME).newInstance();
      assertThat(compiled.toCode(QUALIFIED_NAME), is(code));
   }
}
