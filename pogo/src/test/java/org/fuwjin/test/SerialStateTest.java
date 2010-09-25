package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.fuwjin.pogo.state.PogoPosition;
import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.pogo.state.SerialState;
import org.junit.Test;

/**
 * Demos the serialization state.
 */
public class SerialStateTest {
   /**
    * The serial state should persist a character in a single character advance.
    */
   @Test
   public void testAdvance() {
      final StringBuilder builder = new StringBuilder();
      final PogoState state = new SerialState(builder);
      state.setValue(null);
      assertTrue(state.advance('a', 'a'));
      assertThat(builder.toString(), is("a"));
   }

   /**
    * The serial state should persist the current value in an any character
    * advance.
    */
   @Test
   public void testAdvanceObject() {
      final StringBuilder builder = new StringBuilder();
      final PogoState state = new SerialState(builder);
      state.setValue("test");
      assertTrue(state.advance(0, Integer.MAX_VALUE));
      assertThat(builder.toString(), is("test"));
   }

   /**
    * Test for Issue 1.
    */
   @Test
   public void testAdvanceObjectThenReset() {
      final StringBuilder builder = new StringBuilder();
      final PogoState state = new SerialState(builder);
      PogoPosition mark = state.mark();
      state.setValue("longtest");
      assertTrue(state.advance(0, Integer.MAX_VALUE));
      mark.release();
      mark = state.mark();
      state.setValue("test");
      assertTrue(state.advance(0, Integer.MAX_VALUE));
      mark.reset();
      mark.release();
      assertThat(builder.toString(), is("longtest"));
   }
}
