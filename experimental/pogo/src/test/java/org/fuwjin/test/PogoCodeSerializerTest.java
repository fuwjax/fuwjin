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
import static org.fuwjin.pogo.Grammar.readGrammar;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.fuwjin.complier.RuntimeClassLoader;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoClassLoader;
import org.fuwjin.pogo.PogoClassLoader.QualifiedGenerator;
import org.junit.Test;

/**
 * Tests csv parsing.
 */
public class PogoCodeSerializerTest {
   private static final String QUALIFIED_NAME = "org.fuwjin.generated.PogoGrammar"; //$NON-NLS-1$
   private static final String TEST_POGO = "pogoParse.pogo"; //$NON-NLS-1$
   private static final String CODE_POGO = "pogoCodeSerial.pogo"; //$NON-NLS-1$

   /**
    * Tests that a semi-interesting csv can be digested.
    * @throws Exception if there is a failure
    */
   @Test
   public void testCodeGeneration() throws Exception {
      final Grammar pogo = readGrammar(streamBytes(TEST_POGO));
      final String code = pogo.toCode(QUALIFIED_NAME);
      final RuntimeClassLoader loader = new RuntimeClassLoader();
      assertTrue(loader.compile(QUALIFIED_NAME, code));
      final Grammar compiled = (Grammar)loader.loadClass(QUALIFIED_NAME).newInstance();
      assertThat(compiled.toCode(QUALIFIED_NAME), is(code));
   }

   /**
    * Tests that a semi-interesting csv can be digested with the fancy
    * classloader
    * @throws Exception if there is a failure
    */
   @Test
   public void testFancyCodeGeneration() throws Exception {
      final PogoClassLoader loader = new PogoClassLoader(CODE_POGO);
      final QualifiedGenerator generator = new QualifiedGenerator(QUALIFIED_NAME, readGrammar(streamBytes(TEST_POGO)));
      final Grammar compiled = (Grammar)loader.loadClass(generator).newInstance();
      final Grammar parsed = (Grammar)compiled.parse(streamBytes(TEST_POGO));
      assertThat(compiled.toPogo(), is(parsed.toPogo()));
   }
}
