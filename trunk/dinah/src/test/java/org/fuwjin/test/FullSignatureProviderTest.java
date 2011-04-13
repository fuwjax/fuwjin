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
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.dinah.ReflectiveFunctionProvider;
import org.fuwjin.sample.Sample;
import org.junit.Before;
import org.junit.Test;

public class FullSignatureProviderTest {
   private FunctionProvider provider;

   @Before
   public void setup() {
      provider = new ReflectiveFunctionProvider();
      Sample.staticValue = "initial";
   }

   @Test
   public void testConstructor() throws Exception {
      final Function function = provider.getFunction(new FunctionSignature("org.fuwjin.sample.Sample.new")
            .addArg("java.lang.String"));
      final Object result = function.invoke("test");
      assertThat((Sample)result, is(new Sample("test")));
   }

   @Test
   public void testFieldAccess() throws Exception {
      final Function function = provider.getFunction(new FunctionSignature("org.fuwjin.sample.Sample.value")
            .addArg("org.fuwjin.sample.Sample"));
      final Sample sample = new Sample("test");
      final Object test = function.invoke(sample);
      assertThat((String)test, is("test"));
   }

   @Test
   public void testFieldMutator() throws Exception {
      final Function function = provider.getFunction(new FunctionSignature("org.fuwjin.sample.Sample.value").addArg(
            "org.fuwjin.sample.Sample").addArg("java.lang.String"));
      final Sample sample = new Sample("test");
      function.invoke(sample, "check");
      assertThat(sample, is(new Sample("check")));
   }

   @Test
   public void testInstanceOf() throws Exception {
      final Function function = provider.getFunction(new FunctionSignature("org.fuwjin.sample.Sample.instanceof")
            .addArg("java.lang.Object"));
      final Sample sample = new Sample("test");
      final Object test = function.invoke(sample);
      assertTrue((Boolean)test);
      final Object fail = function.invoke("not a sample");
      assertFalse((Boolean)fail);
   }

   @Test
   public void testMethod() throws Exception {
      final Function function = provider.getFunction(new FunctionSignature("org.fuwjin.sample.Sample.getValue")
            .addArg("org.fuwjin.sample.Sample"));
      final Sample sample = new Sample("test");
      final Object test = function.invoke(sample);
      assertThat((String)test, is("get:test"));
   }

   @Test
   public void testStaticFieldAccess() throws Exception {
      final Function function = provider.getFunction(new FunctionSignature("org.fuwjin.sample.Sample.staticValue"));
      final Object test = function.invoke();
      assertThat((String)test, is("initial"));
   }

   @Test
   public void testStaticFieldMutator() throws Exception {
      final Function function = provider.getFunction(new FunctionSignature("org.fuwjin.sample.Sample.staticValue")
            .addArg("java.lang.String"));
      function.invoke("check");
      assertThat(Sample.staticValue, is("check"));
   }

   @Test
   public void testStaticMethod() throws Exception {
      final Function function = provider.getFunction(new FunctionSignature("org.fuwjin.sample.Sample.doStatic")
            .addArg("java.lang.String"));
      final Object test = function.invoke("test");
      assertThat((String)test, is("initial:test"));
   }
}
