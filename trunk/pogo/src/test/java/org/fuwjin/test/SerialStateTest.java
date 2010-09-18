package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.pogo.state.SerialState;
import org.junit.Test;

public class SerialStateTest {
   @Test
   public void testAdvance() {
      final StringBuilder builder = new StringBuilder();
      final PogoState state = new SerialState(builder);
      state.setValue(null);
      assertTrue(state.advance('a', 'a'));
      assertThat(builder.toString(), is("a"));
   }

   @Test
   public void testAdvanceObject() {
      final StringBuilder builder = new StringBuilder();
      final PogoState state = new SerialState(builder);
      state.setValue("test");
      assertTrue(state.advance(0, Integer.MAX_VALUE));
      assertThat(builder.toString(), is("test"));
   }
}
