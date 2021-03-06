/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.test;

import static org.fuwjin.pogo.CodePointStreamFactory.streamOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.fuwjin.pogo.PogoException;
import org.fuwjin.pogo.state.ParseState;
import org.fuwjin.pogo.state.PogoMemo;
import org.fuwjin.pogo.state.PogoPosition;
import org.fuwjin.pogo.state.PogoState;
import org.junit.Test;

/**
 * Demos the Parse state.
 */
public class ParseStateTest {
   /**
    * The parse state should be able to advance on a matched character.
    */
   @Test
   public void testAdvance() {
      final PogoState state = new ParseState(streamOf("a"));
      assertTrue(state.advance('a', 'a'));
   }

   /**
    * The parse state should be able to advance on an any character if there is
    * a next character.
    */
   @Test
   public void testAnyChar() {
      final PogoState state = new ParseState(streamOf("a"));
      assertTrue(state.advance(0, Integer.MAX_VALUE));
      assertFalse(state.advance(0, Integer.MAX_VALUE));
   }

   /**
    * The parse state should be able to buffer the output.
    */
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

   /**
    * The parse state should allow an advance after a failure.
    */
   @Test
   public void testFailThenAdvance() {
      final PogoState state = new ParseState(streamOf("a"));
      assertFalse(state.advance('b', 'b'));
      assertTrue(state.advance('a', 'a'));
   }

   /**
    * The parse state should support memoization.
    */
   @Test
   public void testMemo() {
      final PogoState state = new ParseState(streamOf("zabc"));
      assertTrue(state.advance('z', 'z'));
      final PogoPosition mark = state.mark();
      PogoPosition buffer = state.buffer(true);
      PogoMemo memo = state.getMemo("memo");
      assertFalse(memo.isStored());
      assertTrue(state.advance('a', 'a'));
      assertTrue(state.advance('b', 'b'));
      state.setValue("value");
      memo.store();
      buffer.reset();
      buffer.release();
      mark.reset();
      mark.release();
      buffer = state.buffer(true);
      memo = state.getMemo("memo");
      assertTrue(memo.isStored());
      assertThat((String)state.getValue(), is("value"));
      assertThat(buffer.toString().toCharArray(), is("ab".toCharArray()));
      buffer.release();
      assertTrue(state.advance('c', 'c'));
   }

   /**
    * The parse state should be able to reset to an earlier state.
    */
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

   /**
    * The parse state should be able to generate an exception after a failure.
    */
   @Test
   public void testRuleFailure() {
      final PogoState state = new ParseState(streamOf("a"));
      final PogoMemo memo = state.getMemo("Parent");
      final PogoPosition mark = state.mark();
      assertTrue(state.advance('a', 'a'));
      state.fail("could not finalize Rule", null);
      mark.reset();
      mark.release();
      memo.fail();
      final PogoException ex = state.exception();
      assertThat(ex.getMessage(), is("Error parsing @[1,2]: could not finalize Rule\n  in Parent[1,1]"));
   }

   /**
    * The parse state should support reset without buffering.
    */
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

   /**
    * The parse state should support failed sub buffers.
    */
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

   /**
    * The parse state should support failed sub buffers.
    */
   @Test
   public void testSubBuffer2() {
      final PogoState state = new ParseState(streamOf("zabc"));
      assertTrue(state.advance('z', 'z'));
      final PogoPosition buffer = state.buffer(true);
      assertTrue(state.advance('a', 'a'));
      buffer.release();
      final PogoPosition sub = state.buffer(true);
      assertTrue(state.advance('b', 'b'));
      sub.release();
      assertThat(sub.toString(), is("b"));
   }
}
