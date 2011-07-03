package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.fuwjin.dinah.FunctionProvider;
import org.fuwjin.dinah.provider.CompositeFunctionProvider;
import org.junit.Before;
import org.junit.Test;

public class ArrayTest {
   private FunctionProvider provider;

   /**
    * Initializes the provider.
    */
   @Before
   public void setup() {
      provider = new CompositeFunctionProvider();
   }

   @Test
   public void testArrayVirtualMethods() throws Exception {
      final Object arr = provider.forName("int[].new").function().invoke(3);
      assertThat((int[])arr, is(new int[3]));
      provider.forName("int[].set").function().invoke(arr, 0, 1);
      assertThat((int[])arr, is(new int[]{1, 0, 0}));
      final Object result = provider.forName("int[].get").function().invoke(arr, 0);
      assertThat((Integer)result, is(1));
      final Object length = provider.forName("int[].length").function().invoke(arr);
      assertThat((Integer)length, is(3));
   }
}
