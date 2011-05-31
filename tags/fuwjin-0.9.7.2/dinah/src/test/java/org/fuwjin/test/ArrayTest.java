package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.fuwjin.dinah.CachedFunctionProvider;
import org.fuwjin.dinah.FunctionProvider;
import org.fuwjin.dinah.signature.NameOnlySignature;
import org.junit.Before;
import org.junit.Test;

public class ArrayTest {
   private FunctionProvider provider;

   /**
    * Initializes the provider.
    */
   @Before
   public void setup() {
      provider = new CachedFunctionProvider();
   }

   @Test
   public void testArrayVirtualMethods() throws Exception {
      final Object arr = provider.getFunction(new NameOnlySignature("int[].new")).invoke(3);
      assertThat((int[])arr, is(new int[3]));
      provider.getFunction(new NameOnlySignature("int[].set")).invoke(arr, 0, 1);
      assertThat((int[])arr, is(new int[]{1, 0, 0}));
      final Object result = provider.getFunction(new NameOnlySignature("int[].get")).invoke(arr, 0);
      assertThat((Integer)result, is(1));
      final Object length = provider.getFunction(new NameOnlySignature("int[].length")).invoke(arr);
      assertThat((Integer)length, is(3));
   }
}
