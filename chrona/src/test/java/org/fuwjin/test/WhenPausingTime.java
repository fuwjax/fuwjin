package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjin.chrona.Chrona;
import org.junit.Test;

public class WhenPausingTime{
   @Test
   public void shouldNotIncrease(){
      Chrona.enable();
      try{
         assertThat(System.currentTimeMillis(),is(0L));
      }finally{
         Chrona.disable();
      }
   }
}
