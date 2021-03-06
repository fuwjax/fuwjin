/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionProvider;
import org.fuwjin.dinah.provider.CompositeFunctionProvider;
import org.fuwjin.sample.Sample;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the functionality around the fully specified signatures.
 */
public class FullSignatureProviderTest {
   private FunctionProvider provider;

   /**
    * Initializes the provider.
    */
   @Before
   public void setup() {
      provider = new CompositeFunctionProvider();
      Sample.staticValue = "initial";
   }

   /**
    * Tests a fully qualified constructor signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testConstructor() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.new").withNextArg("java.lang.String")
            .function();
      final Object result = function.invoke("test");
      assertThat((Sample)result, is(new Sample("test")));
   }

   /**
    * Tests a fully qualified field access signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testFieldAccess() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.value")
            .withNextArg("org.fuwjin.sample.Sample").function();
      final Sample sample = new Sample("test");
      final Object test = function.invoke(sample);
      assertThat((String)test, is("test"));
   }

   /**
    * Tests a fully qualified field mutator signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testFieldMutator() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.value")
            .withNextArg("org.fuwjin.sample.Sample").withNextArg("java.lang.String").function();
      final Sample sample = new Sample("test");
      function.invoke(sample, "check");
      assertThat(sample, is(new Sample("check")));
   }

   /**
    * Tests a fully qualified instanceof signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testInstanceOf() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.instanceof").withNextArg("java.lang.Object")
            .function();
      final Sample sample = new Sample("test");
      final Object test = function.invoke(sample);
      assertTrue((Boolean)test);
      final Object fail = function.invoke("not a sample");
      assertFalse((Boolean)fail);
   }

   /**
    * Tests a fully qualified method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testMethod() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.getValue")
            .withNextArg("org.fuwjin.sample.Sample").function();
      final Sample sample = new Sample("test");
      final Object test = function.invoke(sample);
      assertThat((String)test, is("get:test"));
   }

   /**
    * Tests a fully qualified static field access signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testStaticFieldAccess() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.staticValue").function();
      final Object test = function.invoke();
      assertThat((String)test, is("initial"));
   }

   /**
    * Tests a fully qualified static field mutator signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testStaticFieldMutator() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.staticValue")
            .withNextArg("java.lang.String").function();
      function.invoke("check");
      assertThat(Sample.staticValue, is("check"));
   }

   /**
    * Tests a fully qualified static method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testStaticMethod() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.doStatic").withNextArg("java.lang.String")
            .function();
      final Object test = function.invoke("test");
      assertThat((String)test, is("initial:test"));
   }
}
