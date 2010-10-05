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
