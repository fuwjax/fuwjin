package org.fuwjin.test;

import static org.fuwjin.sample.SampleSystem.postTest;
import static org.fuwjin.sample.SampleSystem.preTest;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BespectTest{
   @Test(timeout=100) 
   public void testPostAdvice(){
      assertThat(postTest(), is(12345L));
   }

   @Test(timeout=100) 
   public void testPreAdvice(){
      StringBuilder builder = new StringBuilder("Hello world");
      assertThat(preTest(builder), is("Hi Mom"));
   }
}
