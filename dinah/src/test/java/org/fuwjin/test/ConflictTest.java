package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionProvider;
import org.fuwjin.dinah.ReflectiveFunctionProvider;
import org.fuwjin.dinah.function.MethodFunction;
import org.fuwjin.dinah.signature.TypedArgsSignature;
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
      provider = new ReflectiveFunctionProvider();
      Sample.staticValue = "initial";
   }

   @Test
   @Ignore
   public void testAppendOverlap() throws Exception {
      final Function function = provider.getFunction(new TypedArgsSignature("java.lang.StringBuilder.append").addArg(
            "java.lang.StringBuilder").addArg("java.lang.Object"));
      assertTrue(function.getClass().getCanonicalName(), function instanceof MethodFunction);
   }

   /**
    * Tests overlapping signatures. Technically, there is no reason to prefer
    * the static method to come first.
    * @throws Exception if the test fails
    */
   @Test
   public void testOverlap() throws Exception {
      final Function function = provider.getFunction(new TypedArgsSignature("org.fuwjin.sample.SameName.name").addArg(
            "org.fuwjin.sample.SameName").addArg("java.lang.String"));
      final SameName name = new SameName();
      function.invoke(name, "test");
      assertThat(name.name, is("static:test"));
   }
}
