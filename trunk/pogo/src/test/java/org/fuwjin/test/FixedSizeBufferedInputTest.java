package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.StringReader;

import org.fuwjin.io.FixedSizeBufferedInput;
import org.junit.Test;

public class FixedSizeBufferedInputTest {
   private final String str = "abcdefghijklmnopqrstuvwxyz";
   private final CharSequence seq = FixedSizeBufferedInput.buffer(10, new StringReader(str));

   @Test
   public void testBackUpTooFar() {
      seq.charAt(15);
      try {
         seq.charAt(5);
         fail("should not be able to back up beyond the buffer size");
      } catch(final IndexOutOfBoundsException e) {
         // pass
      }
      try {
         seq.subSequence(5, 10);
         fail("should not be able to back up beyond the buffer size");
      } catch(final IndexOutOfBoundsException e) {
         // pass
      }
      try {
         seq.subSequence(10, 17);
         fail("should not be able to back up beyond the buffer size");
      } catch(final IndexOutOfBoundsException e) {
         // pass
      }
   }

   @Test
   public void testBounds() {
      try {
         seq.charAt(-1);
         fail("no negative indexes");
      } catch(final IndexOutOfBoundsException e) {
         // pass
      }
      try {
         seq.charAt(26);
         fail("no input");
      } catch(final IndexOutOfBoundsException e) {
         // pass
      }
   }

   @Test
   public void testCharAt() {
      for(int i = 0; i < str.length(); i++) {
         assertThat(seq.charAt(i), is(str.charAt(i)));
      }
   }

   @Test
   public void testLength() {
      assertThat(seq.length(), is(0));
      for(int i = 0; i < str.length(); i++) {
         seq.charAt(i);
         assertThat(seq.length(), is(i + 1));
      }
   }

   @Test
   public void testSubBounds() {
      seq.charAt(8);
      try {
         seq.subSequence(-1, 8);
         fail("no negative indexes");
      } catch(final IndexOutOfBoundsException e) {
         // pass
      }
      try {
         seq.subSequence(8, 5);
         fail("start over end");
      } catch(final IndexOutOfBoundsException e) {
         // pass
      }
   }

   @Test
   public void testSubSequence() {
      for(int i = 5; i < str.length(); i++) {
         seq.charAt(i);
         assertThat(seq.subSequence(i - 5, i), is(str.subSequence(i - 5, i)));
      }
   }
}
