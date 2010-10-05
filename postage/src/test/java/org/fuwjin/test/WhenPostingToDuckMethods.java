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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.fuwjin.postage.Failure.FailureException;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.category.DuckCategory;
import org.junit.Before;
import org.junit.Test;

public class WhenPostingToDuckMethods {
   private Postage postage;

   @Before
   public void setup() {
      postage = new Postage(new DuckCategory("true"));
   }

   @Test
   public void shouldDuckTypeAppropriately() throws FailureException {
      final String str = "test";
      final Function func = postage.getFunction("true.length");
      assertThat((Integer)func.invoke(str), is(str.length()));
   }

   @Test
   public void shouldFailOnNoTarget() {
      final Function func = postage.getFunction("true.anything");
      try {
         func.invoke();
         fail("should fail");
      } catch(final FailureException e) {
         // pass
      }
   }

   @Test
   public void shouldFailOnNullTarget() {
      final Function func = postage.getFunction("true.anything");
      try {
         func.invoke((Object)null);
         fail("should fail");
      } catch(final FailureException e) {
         // pass
      }
   }
}
