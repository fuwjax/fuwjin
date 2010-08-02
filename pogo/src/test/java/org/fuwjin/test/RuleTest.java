/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static org.fuwjin.pogo.PogoUtils._new;
import static org.fuwjin.pogo.PogoUtils.init;
import static org.fuwjin.pogo.PogoUtils.lit;
import static org.fuwjin.pogo.PogoUtils.result;
import static org.fuwjin.pogo.PogoUtils.rule;
import static org.fuwjin.pogo.PogoUtils.type;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import org.fuwjin.io.PogoException;
import org.fuwjin.pogo.Grammar;
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
            add(rule("Grammar", type(), init(), result(), lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(new StringReader(A));
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
            add(rule("Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result("builder"), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
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
            add(rule("Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result("toResult"), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
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
            add(rule("Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result(), lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(new StringReader(A));
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
                  "Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), init(), result("newInstance"), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
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
            add(rule("Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), init(), result("NULL"), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
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
            add(rule("Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), init(), result("valueOf"), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
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
                  "Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), init("newInstance"), result(), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("static newInstance;")); //$NON-NLS-1$
   }
}
