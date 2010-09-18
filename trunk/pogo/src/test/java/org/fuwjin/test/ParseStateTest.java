package org.fuwjin.test;

import static org.fuwjin.pogo.CodePointStreamFactory.streamOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.fuwjin.pogo.PogoException;
import org.fuwjin.pogo.state.PogoMemo;
import org.fuwjin.pogo.state.ParseState;
import org.fuwjin.pogo.state.PogoPosition;
import org.fuwjin.pogo.state.PogoState;
import org.junit.Test;

public class ParseStateTest {
   @Test
   public void testAdvance() {
      final PogoState state = new ParseState(streamOf("a"));
      assertTrue(state.advance('a', 'a'));
   }

   @Test
   public void testAnyChar() {
      final PogoState state = new ParseState(streamOf("a"));
      assertTrue(state.advance(0, Integer.MAX_VALUE));
      assertFalse(state.advance(0, Integer.MAX_VALUE));
   }

   @Test
   public void testBuffer() {
      final PogoState state = new ParseState(streamOf("abc"));
      final PogoPosition buffer = state.buffer(true);
      assertTrue(state.advance('a', 'a'));
      assertTrue(state.advance('b', 'b'));
      assertTrue(state.advance('c', 'c'));
      assertThat(buffer.toString(), is("abc"));
      buffer.release();
   }

   @Test
   public void testFailThenAdvance() {
      final PogoState state = new ParseState(streamOf("a"));
      assertFalse(state.advance('b', 'b'));
      assertTrue(state.advance('a', 'a'));
   }

   @Test
   public void testMemo() {
      final PogoState state = new ParseState(streamOf("zabc"));
      assertTrue(state.advance('z', 'z'));
      final PogoMemo memo = state.getMemo("memo", true);
      assertFalse(memo.isStored());
      final PogoPosition buffer = state.buffer(true);
      assertTrue(state.advance('a', 'a'));
      assertTrue(state.advance('b', 'b'));
      memo.store(buffer.toString(), "value");
      buffer.release();
      assertTrue(memo.isStored());
      assertThat(memo.buffer(), is("ab"));
      assertThat((String)memo.value(), is("value"));
   }

   @Test
   public void testMemoed() {
      final PogoState state = new ParseState(streamOf("zabc"));
      assertTrue(state.advance('z', 'z'));
      final PogoPosition mark = state.mark();
      PogoMemo memo = state.getMemo("memo", true);
      final PogoPosition buffer = state.buffer(true);
      assertTrue(state.advance('a', 'a'));
      assertTrue(state.advance('b', 'b'));
      memo.store(buffer.toString(), "value");
      buffer.release();
      mark.reset();
      mark.release();
      memo = state.getMemo("memo", true);
      assertTrue(memo.isStored());
      assertThat(memo.buffer(), is("ab"));
      assertThat((String)memo.value(), is("value"));
      assertTrue(state.advance('c', 'c'));
   }

   @Test
   public void testReset() {
      final PogoState state = new ParseState(streamOf("a"));
      final PogoPosition mark = state.mark();
      assertTrue(state.advance('a', 'a'));
      mark.reset();
      assertTrue(state.advance('a', 'a'));
      mark.release();
      assertFalse(state.advance('a', 'a'));
   }

   @Test
   public void testRuleFailure() {
      final PogoState state = new ParseState(streamOf("a"));
      final PogoMemo memo = state.getMemo("Parent", false);
      assertTrue(state.advance('a', 'a'));
      state.fail("could not finalize Rule", null);
      memo.fail();
      final PogoException ex = state.exception();
      assertThat(ex.getMessage(), is("Error parsing @[1,2]: could not finalize Rule\n  in Parent[1,1]"));
   }

   @Test
   public void testSequenceReset() {
      final PogoState state = new ParseState(streamOf("a"));
      final PogoPosition mark = state.current();
      assertTrue(state.advance('a', 'a'));
      mark.reset();
      try {
         state.advance('a', 'a');
         fail();
      } catch(final IllegalStateException e) {
         // pass
      }
   }

   @Test
   public void testSubBuffer() {
      final PogoState state = new ParseState(streamOf("zabc"));
      assertTrue(state.advance('z', 'z'));
      final PogoPosition buffer = state.buffer(true);
      assertTrue(state.advance('a', 'a'));
      final PogoPosition sub = state.buffer(true);
      final PogoPosition mark = state.mark();
      assertTrue(state.advance('b', 'b'));
      assertThat(sub.toString(), is("b"));
      mark.reset();
      mark.release();
      sub.reset();
      sub.release();
      assertTrue(state.advance('b', 'b'));
      assertTrue(state.advance('c', 'c'));
      assertThat(buffer.toString(), is("abc"));
      buffer.release();
   }
}
