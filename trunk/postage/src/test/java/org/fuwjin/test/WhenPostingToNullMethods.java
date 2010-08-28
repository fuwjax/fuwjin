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
   public void shouldFailOnArgs() {
      final Function func = postage.getFunction("null", "instanceof");
      try {
         func.invoke();
         fail("should fail");
      } catch(final FailureException e) {
         // pass
      }
   }

   @Test
   public void shouldFailOnNonNull() throws FailureException {
      final Function func = postage.getFunction("null", "instanceof");
      assertTrue(!(Boolean)func.invoke("test"));
   }

   @Test
   public void shouldReturnNull() throws FailureException {
      final Function func = postage.getFunction("null", "anything");
      assertNull(func.invoke());
      assertNull(func.invoke("anything"));
   }

   @Test
   public void shouldReturnNullInstanceOf() throws FailureException {
      final Function func = postage.getFunction("null", "instanceof");
      assertTrue((Boolean)func.invoke((Object)null));
   }
}
