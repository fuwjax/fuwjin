package org.fuwjin.test;

import static org.fuwjin.sample.SampleSystem.startTime;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BespectTest{
   @Test(timeout=100)
   public void testSystemAdvice(){
      assertThat(startTime(), is(12345L));
   }
}
