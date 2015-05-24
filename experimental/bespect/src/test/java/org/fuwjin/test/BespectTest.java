package org.fuwjin.test;

import static org.fuwjin.sample.SampleSystem.getTime;
import static org.fuwjin.sample.SampleSystem.builderToString;
import static org.fuwjin.sample.SampleSystem.addTwoNumbers;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BespectTest{
   @Test(timeout=100) 
   public void testPostAdvice(){
      assertThat(getTime(), is(12345L));
   }

   @Test(timeout=100) 
   public void testPreAdvice(){
      StringBuilder builder = new StringBuilder("Hello world");
      assertThat(builderToString(builder), is("Hi Mom"));
      assertThat(builder.toString(), is("Hi Mom"));
   }
   
   @Test(timeout=100)
   public void testReplaceAdvice(){
      assertThat(addTwoNumbers(3,7), is(21));
   }
}
