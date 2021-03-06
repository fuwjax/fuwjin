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
import static org.fuwjin.pogo.LiteratePogo.init;
import static org.fuwjin.pogo.LiteratePogo.initRef;
import static org.fuwjin.pogo.LiteratePogo.lit;
import static org.fuwjin.pogo.LiteratePogo.ref;
import static org.fuwjin.pogo.LiteratePogo.resultRef;
import static org.fuwjin.pogo.LiteratePogo.rule;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoException;
import org.junit.Test;

/**
 * Tests the rule reference.
 */
public class RuleReferenceTest {
   private static final String A = "a"; //$NON-NLS-1$

   /**
    * Tests append finalization.
    * @throws PogoException if it fails
    */
   @Test
   public void testAddMethod() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", org.fuwjin.test.SampleBuilderPattern.class.getCanonicalName()).add(init("new")).expression(ref("Rule").add(resultRef("addChild")))); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            add(rule("Rule", org.fuwjin.test.SampleBuilderPattern.class.getCanonicalName()).add(init("new")).expression(lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is("default constructor;adding:default constructor;;")); //$NON-NLS-1$
   }

   /**
    * Tests constructor initialization.
    * @throws PogoException if it fails
    */
   @Test
   public void testNewChild() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", org.fuwjin.test.SampleBuilderPattern.class.getCanonicalName()).add(init("new")).expression(ref("Rule").add(initRef("newChild")).add(resultRef("addChild")))); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
            add(rule("Rule", org.fuwjin.test.SampleBuilderPattern.class.getCanonicalName()).add(init("instanceof")).expression(lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is("default constructor;adding:new child;;")); //$NON-NLS-1$
   }

   /**
    * Tests pass back finalization.
    * @throws PogoException if it fails
    */
   @Test
   public void testReturn() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", Object.class.getCanonicalName()).expression(ref("Rule").add(resultRef("return")))); //$NON-NLS-1$//$NON-NLS-2$
            add(rule("Rule", org.fuwjin.test.SampleBuilderPattern.class.getCanonicalName()).add(init("new")).expression(lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is("default constructor;")); //$NON-NLS-1$
   }

   /**
    * Tests pass through initialization.
    * @throws PogoException if it fails
    */
   @Test
   public void testThis() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", org.fuwjin.test.SampleBuilderPattern.class.getCanonicalName()).add(init("new")).expression(ref("Rule").add(initRef("this")).add(resultRef("addChild")))); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            add(rule("Rule", org.fuwjin.test.SampleBuilderPattern.class.getCanonicalName()).add(init("instanceof")).expression(lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is("default constructor;adding:default constructor;;")); //$NON-NLS-1$
   }
}
