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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.fuwjin.postage.Failure.FailureException;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.category.ReflectionCategory;
import org.fuwjin.postage.type.Optional;
import org.fuwjin.sample.CollisionObject;
import org.junit.Before;
import org.junit.Test;

public class WhenPostingToOptionalClassMethods {
   private Postage postage;

   @Before
   public void setup() {
      postage = new Postage(new ReflectionCategory());
      CollisionObject.someName = "test";
   }

   @Test
   public void shouldPostMessagesWithOptionals() throws FailureException {
      final Function func = postage.getFunction(CollisionObject.class.getCanonicalName() + ".someName").withSignature(
            String.class, new Optional(CollisionObject.class), new Optional(String.class));
      assertThat((String)func.invoke(Optional.UNSET, Optional.UNSET), is("test"));
      assertEquals(func.returnType(), String.class);
      assertThat((String)func.invoke(new CollisionObject(), "success"), is("success"));
      assertThat((String)func.invoke(Optional.UNSET, "success"), is("test"));
   }

   @Test
   public void shouldPostMessagesWithoutRestriction() throws FailureException {
      final Function func = postage.getFunction(CollisionObject.class.getCanonicalName() + ".someName");
      assertThat((String)func.invoke(), is("test"));
      assertNull(func.invoke("newTest"));
      assertThat((String)func.invoke(), is("newTest"));
      assertThat((Long)func.invoke(7L), is(7L));
   }

   @Test
   public void shouldPostMessagesWithRestriction() throws FailureException {
      final Function func = postage.getFunction(CollisionObject.class.getCanonicalName() + ".someName").withSignature(
            String.class);
      assertThat((String)func.invoke(), is("test"));
      assertEquals(func.returnType(), String.class);
      try {
         func.invoke(new CollisionObject(), "fail");
         fail();
      } catch(final FailureException e) {
         // pass
      }
   }
}
