package org.fuwjin.test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.fail;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;
import org.junit.Before;
import org.junit.Test;

public class WhenPostingToNullMethods {
   private Postage postage;

   @Before
   public void setup() {
      postage = new Postage();
   }

   @Test
   public void shouldFailOnArgs() {
      final Function func = postage.getFunction("null", "instanceof");
      try {
         func.invoke();
         fail("should fail");
      } catch(final Failure e) {
         // pass
      }
   }

   @Test
   public void shouldFailOnNonNull() {
      final Function func = postage.getFunction("null", "instanceof");
      try {
         func.invoke("test");
         fail("should fail");
      } catch(final Failure e) {
         // pass
      }
   }

   @Test
   public void shouldReturnNull() throws Failure {
      final Function func = postage.getFunction("null", "anything");
      assertNull(func.invoke());
      assertNull(func.invoke("anything"));
   }

   @Test
   public void shouldReturnNullInstanceOf() throws Failure {
      final Function func = postage.getFunction("null", "instanceof");
      assertNull(func.invoke((Object)null));
   }
}
