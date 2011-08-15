package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionProvider;
import org.fuwjin.dinah.function.MethodFunction;
import org.fuwjin.dinah.provider.CompositeFunctionProvider;
import org.fuwjin.sample.SameName;
import org.fuwjin.sample.Sample;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests what happens when the signatures completely overlap.
 */
public class ConflictTest {
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
    * I'm not sure what the desired behavior should be here.
    * @throws Exception if the test fails
    */
   @Test
   @Ignore
   public void testAppendOverlap() throws Exception {
      final Function function = provider.forName("java.lang.StringBuilder.append")
            .withNextArg("java.lang.StringBuilder").withNextArg("java.lang.Object").function();
      assertTrue(function.getClass().getCanonicalName(), function instanceof MethodFunction);
   }

   /**
    * Tests overlapping signatures. Technically, there is no reason to prefer
    * the static method to come first.
    * @throws Exception if the test fails
    */
   @Test
   public void testOverlap() throws Exception {
      final Function function = provider.forName("org.fuwjin.sample.SameName.name")
            .withNextArg("org.fuwjin.sample.SameName").withNextArg("java.lang.String").function();
      final SameName name = new SameName();
      function.invoke(name, "test");
      assertThat(name.name, is("static:test"));
   }
}
