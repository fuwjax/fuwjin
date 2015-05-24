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

import static org.junit.Assert.fail;

import org.fuwjin.postage.Failure.FailureException;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.category.VoidCategory;
import org.junit.Before;
import org.junit.Test;

public class WhenPostingToVoidMethods {
   private Postage postage;

   @Before
   public void setup() {
      postage = new Postage(new VoidCategory("void"));
   }

   @Test
   public void shouldFailAlways() {
      final Function func = postage.getFunction("void.anything");
      try {
         func.invoke();
         fail("should fail");
      } catch(final FailureException e) {
         // pass
      }
   }

   @Test
   public void shouldFailAlwaysWithTypes() {
      final Function func = postage.getFunction("void.anything");
      try {
         func.invoke("test");
         fail("should fail");
      } catch(final FailureException e) {
         // pass
      }
   }
}
