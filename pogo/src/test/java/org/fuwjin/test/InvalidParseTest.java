/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static org.fuwjin.pogo.CodePointStreamFactory.streamOf;
import static org.fuwjin.pogo.LiteratePogo.lit;
import static org.fuwjin.pogo.LiteratePogo.rule;
import static org.fuwjin.pogo.LiteratePogo.seq;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoException;
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
               add(rule("Grammar", Object.class, "default", "default", "default", seq(lit('a'), lit('b')))); //$NON-NLS-1$
               resolve();
            }
         }.parse(streamOf("ac"));
      } catch(final PogoException e) {
         assertThat(e.getMessage(), is("Error parsing Grammar[1,2]: failed test: 'c' expecting 'b'"));
      }
   }
}
