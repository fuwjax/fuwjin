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

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.fuwjin.postage.Failure.FailureException;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.category.ConstantCategory;
import org.junit.Before;
import org.junit.Test;

public class WhenPostingToNullMethods {
   private Postage postage;

   @Before
   public void setup() {
      postage = new Postage(new ConstantCategory());
   }

   @Test
   public void shouldBeFalseOnNonNull() throws FailureException {
      final Function func = postage.getFunction("null.equals");
      assertTrue(!(Boolean)func.invoke("test"));
   }

   @Test
   public void shouldFailOnArgs() {
      final Function func = postage.getFunction("null.equals");
      try {
         func.invoke();
         fail("should fail");
      } catch(final FailureException e) {
         // pass
      }
   }

   @Test
   public void shouldFailOnNullWithExtraArgs() throws FailureException {
      final Function func = postage.getFunction("null.anything").withSignature(Object.class);
      try {
         func.invoke("anything");
         fail("should fail");
      } catch(final FailureException e) {
         // pass
      }
   }

   @Test
   public void shouldFailOnNullWithMissingArgs() throws FailureException {
      final Function func = postage.getFunction("null.anything").withSignature(Object.class, String.class);
      try {
         func.invoke();
         fail("should fail");
      } catch(final FailureException e) {
         // pass
      }
   }

   @Test
   public void shouldFailOnParams() {
      final Function func = postage.getFunction("null.equals");
      try {
         func.invoke();
         fail("should fail");
      } catch(final FailureException e) {
         // pass
      }
   }

   @Test
   public void shouldReturnNull() throws FailureException {
      final Function func = postage.getFunction("null.anything");
      assertNull(func.invoke());
   }

   @Test
   public void shouldReturnNullInstanceOf() throws FailureException {
      final Function func = postage.getFunction("null.equals");
      assertTrue((Boolean)func.invoke((Object)null));
   }

   @Test
   public void shouldReturnNullWithArgs() throws FailureException {
      final Function func = postage.getFunction("null.anything");
      assertNull(func.invoke("anything"));
   }
}
