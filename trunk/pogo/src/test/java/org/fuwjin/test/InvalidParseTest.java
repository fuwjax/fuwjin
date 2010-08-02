/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static org.fuwjin.pogo.PogoUtils.init;
import static org.fuwjin.pogo.PogoUtils.lit;
import static org.fuwjin.pogo.PogoUtils.result;
import static org.fuwjin.pogo.PogoUtils.rule;
import static org.fuwjin.pogo.PogoUtils.seq;
import static org.fuwjin.pogo.PogoUtils.type;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringReader;
import java.text.ParseException;

import org.fuwjin.io.PogoException;
import org.fuwjin.pogo.Grammar;
import org.junit.Test;

/**
 * Tests Rule parsing.
 */
public class InvalidParseTest {
   /**
    * The stock parse.
    * @throws ParseException if it fails
    */
   @Test
   public void testDefault() throws PogoException {
      try {
         new Grammar() {
            {
               add(rule("Grammar", type(), init(), result(), seq(lit('a'), lit('b')))); //$NON-NLS-1$
               resolve();
            }
         }.parse(new StringReader("ac"));
      } catch(final PogoException e) {
         assertThat(e.getMessage(), is("Error parsing Grammar[1,2]: failed test: 'c' expecting 'b'"));
      }
   }
}
