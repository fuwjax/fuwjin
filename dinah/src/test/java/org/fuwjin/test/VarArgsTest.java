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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionProvider;
import org.fuwjin.dinah.FunctionProvider.NoSuchFunctionException;
import org.fuwjin.dinah.provider.CompositeFunctionProvider;
import org.fuwjin.sample.Sample;
import org.fuwjin.sample.VarArgsSample;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the functionality around the fully specified signatures.
 */
public class VarArgsTest {
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
      final Function function = provider.forName("org.fuwjin.sample.VarArgsSample.new").withNextArg("java.lang.String")
            .function();
      final Object result = function.invoke("test");
      assertThat((VarArgsSample)result, is(new VarArgsSample("test")));
   }

   /**
    * Tests a fully qualified constructor signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testConstructorArray() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.VarArgsSample.new")
            .withNextArg("java.lang.String[]").function();
      final Object result = function.invoke((Object)new String[]{"test"});
      assertThat((VarArgsSample)result, is(new VarArgsSample("test")));
   }

   /**
    * Tests a fully qualified method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testFailingAccess() throws Exception {
      try {
         provider.forName("java.lang.Class.new").function();
         fail("shouldn't work");
      } catch(final NoSuchFunctionException e) {
         // pass
      }
   }

   /**
    * Tests a fully qualified method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testFailingType() throws Exception {
      try {
         provider.forName("java.lang.Fuw.jax").function();
         fail("shouldn't work");
      } catch(final NoSuchFunctionException e) {
         // pass
      }
   }

   /**
    * Tests a fully qualified method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testInterfaceMethod() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.VarArgsInterface.VarArgsChild.setValues")
            .withNextArg("org.fuwjin.sample.VarArgsInterface.VarArgsChild").withNextArg("java.lang.String").function();
      final VarArgsSample obj = new VarArgsSample("ignore");
      function.invoke(obj, "test");
      assertThat(obj, is(new VarArgsSample("test")));
   }

   /**
    * Tests a fully qualified method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testInterfaceMethodArray() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.VarArgsInterface.VarArgsChild.setValues")
            .withNextArg("org.fuwjin.sample.VarArgsInterface.VarArgsChild").withNextArg("java.lang.String[]")
            .function();
      final VarArgsSample obj = new VarArgsSample("ignore");
      function.invoke(obj, new String[]{"test"});
      assertThat(obj, is(new VarArgsSample("test")));
   }

   /**
    * Tests a fully qualified method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testMethod() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.VarArgsSample.setValues")
            .withNextArg("org.fuwjin.sample.VarArgsSample").withNextArg("java.lang.String").function();
      final VarArgsSample obj = new VarArgsSample("ignore");
      function.invoke(obj, "test");
      assertThat(obj, is(new VarArgsSample("test")));
   }

   /**
    * Tests a fully qualified method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testMethodArray() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.VarArgsSample.setValues")
            .withNextArg("org.fuwjin.sample.VarArgsSample").withNextArg("java.lang.String[]").function();
      final VarArgsSample obj = new VarArgsSample("ignore");
      function.invoke(obj, new String[]{"test"});
      assertThat(obj, is(new VarArgsSample("test")));
   }

   /**
    * Tests a fully qualified method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testStaticMethod() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.VarArgsSample.join")
            .withNextArg("java.lang.String").function();
      final Object value = function.invoke("test");
      assertThat((String)value, is("[test]"));
   }

   /**
    * Tests a fully qualified method signature.
    * @throws Exception if the test fails
    */
   @Test
   public void testStaticMethodArray() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.VarArgsSample.join")
            .withNextArg("java.lang.String[]").function();
      final Object value = function.invoke((Object)new String[]{"test"});
      assertThat((String)value, is("[test]"));
   }
}
