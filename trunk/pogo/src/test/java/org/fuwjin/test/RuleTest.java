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
import static org.fuwjin.pogo.LiteratePogo.lit;
import static org.fuwjin.pogo.LiteratePogo.rule;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoException;
import org.junit.Test;

/**
 * Tests Rule parsing.
 */
public class RuleTest {
   private static final String A = "a"; //$NON-NLS-1$

   /**
    * The stock parse.
    * @throws PogoException if it fails
    */
   @Test
   public void testDefault() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", Object.class, "default", "return", "default", lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is(A));
   }

   /**
    * Tests field finaliation.
    * @throws PogoException if it fails
    */
   @Test
   public void testInstanceFieldFinalizer() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", org.fuwjin.test.SampleBuilderPattern.class, "new", "default", "builder", lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj, is(StringBuilder.class));
      assertThat(obj.toString(), is("default constructor;")); //$NON-NLS-1$
   }

   /**
    * Tests method finalization.
    * @throws PogoException if it fails
    */
   @Test
   public void testInstanceFinalizer() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", org.fuwjin.test.SampleBuilderPattern.class, "new", "default", "toResult", lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is("default constructor;toResult;")); //$NON-NLS-1$
   }

   /**
    * Tests default constructor initialization.
    * @throws PogoException if it fails
    */
   @Test
   public void testNew() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", org.fuwjin.test.SampleBuilderPattern.class, "new", "default", "default", lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is("default constructor;")); //$NON-NLS-1$
   }

   /**
    * Tests the static method finalizer.
    * @throws PogoException if it fails
    */
   @Test
   public void testStaticConstantFinalizer() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule(
                  "Grammar", org.fuwjin.test.SampleBuilderPattern.class, "default", "default", "newInstance", lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is("static newInstance;")); //$NON-NLS-1$
   }

   /**
    * Tests the static field finalizer.
    * @throws PogoException if it fails
    */
   @Test
   public void testStaticFieldFinalizer() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", org.fuwjin.test.SampleBuilderPattern.class, "default", "default", "NULL", lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is("static NULL;")); //$NON-NLS-1$
   }

   /**
    * Tests static method finalizer.
    * @throws PogoException if it fails
    */
   @Test
   public void testStaticFinalizer() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", org.fuwjin.test.SampleBuilderPattern.class, "default", "valueOf", "default", lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is("static valueOf:a;")); //$NON-NLS-1$
   }

   /**
    * Tests static initialization.
    * @throws PogoException if it fails
    */
   @Test
   public void testStaticInitializer() throws PogoException {
      final Object obj = new Grammar() {
         {
            add(rule(
                  "Grammar", org.fuwjin.test.SampleBuilderPattern.class, "newInstance", "default", "default", lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(streamOf(A));
      assertThat(obj.toString(), is("static newInstance;")); //$NON-NLS-1$
   }
}
