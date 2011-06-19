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
 * Tests the functionality around name only signatures.
 */
public class NameOnlyProviderTest {
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
    * Tests a name only constructor signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testConstructor() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.new").withArgCount(1).function();
      final Object result = function.invoke("test");
      assertThat((Sample)result, is(new Sample("test")));
   }

   /**
    * Tests a name only field access signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testFieldAccess() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.value").withArgCount(1).function();
      final Sample sample = new Sample("test");
      final Object test = function.invoke(sample);
      assertThat((String)test, is("test"));
   }

   /**
    * Tests a name only field mutator signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testFieldMutator() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.value").withArgCount(2).function();
      final Sample sample = new Sample("test");
      function.invoke(sample, "check");
      assertThat(sample, is(new Sample("check")));
   }

   /**
    * Tests a name only instanceof signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testInstanceOf() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.instanceof").withArgCount(1).function();
      final Sample sample = new Sample("test");
      final Object test = function.invoke(sample);
      assertTrue((Boolean)test);
      final Object fail = function.invoke("not a sample");
      assertFalse((Boolean)fail);
   }

   /**
    * Tests a name only method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testMethod() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.getValue").withArgCount(1).function();
      final Sample sample = new Sample("test");
      final Object test = function.invoke(sample);
      assertThat((String)test, is("get:test"));
   }

   /**
    * Tests a name only static field access signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testStaticFieldAccess() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.staticValue").withArgCount(0).function();
      final Object test = function.invoke();
      assertThat((String)test, is("initial"));
   }

   /**
    * Tests a name only static field mutator signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testStaticFieldMutator() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.staticValue").withArgCount(1).function();
      function.invoke("check");
      assertThat(Sample.staticValue, is("check"));
   }

   /**
    * Tests a name only static method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testStaticMethod() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.Sample.doStatic").withArgCount(1).function();
      final Object test = function.invoke("test");
      assertThat((String)test, is("initial:test"));
   }
}
