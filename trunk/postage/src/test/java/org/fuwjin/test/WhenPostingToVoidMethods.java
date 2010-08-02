package org.fuwjin.test;

import static org.junit.Assert.fail;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;
import org.junit.Before;
import org.junit.Test;

public class WhenPostingToVoidMethods {
   private Postage postage;

   @Before
   public void setup() {
      postage = new Postage();
   }

   @Test
   public void shouldFailAlways() {
      final Function func = postage.getFunction("void", "anything");
      try {
         func.invoke();
         fail("should fail");
      } catch(final Failure e) {
         // pass
      }
   }
}
