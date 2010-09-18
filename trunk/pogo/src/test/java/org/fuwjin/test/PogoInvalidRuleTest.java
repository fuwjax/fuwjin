/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static org.fuwjin.pogo.LiteratePogo.ref;
import static org.fuwjin.pogo.LiteratePogo.rule;
import static org.junit.Assert.fail;

import org.fuwjin.pogo.Grammar;
import org.junit.Test;

/**
 * Tests invalid rules for pogo.
 */
public class PogoInvalidRuleTest {
   /**
    * Tests behavior for an unknown rule.
    */
   @Test
   public void testUnknownRule() {
      new Grammar() {
         {
            add(rule(
                  "Grammar", org.fuwjin.test.SampleBuilderPattern.class, "new", "default", "default", ref("Rule", "default", "default", "addChild"))); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            try {
               resolve();
               fail("Should throw an exception"); //$NON-NLS-1$
            } catch(final RuntimeException e) {
               // pass
            }
         }
      }.toString();
   }
}
