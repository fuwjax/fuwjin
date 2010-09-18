package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjin.util.IntRangeSet;
import org.junit.Test;

public class IntSetTest {
   @Test
   public void testAdjacentChars() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(66, 72);
      set.unionRange(65, 65);
      set.unionRange(73, 73);
      assertThat(set.toString(), is("A-I"));
   }

   @Test
   public void testAdjacentRange() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(69, 72);
      set.unionRange(66, 68);
      set.unionRange(73, 75);
      assertThat(set.toString(), is("B-K"));
   }

   @Test
   public void testAlpha() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(65, 65);
      for(char i = 66; i < 91; i++) {
         set.unionRange(i, i);
         assertThat(set.toString(), is("A-" + i));
      }
   }

   @Test
   public void testAlphaRange() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(65, 65);
      for(char i = 70; i < 91; i += 5) {
         set.unionRange(i - 4, i);
         assertThat(set.toString(), is("A-" + i));
      }
   }

   @Test
   public void testContainedRange() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(65, 68);
      set.unionRange(66, 67);
      assertThat(set.toString(), is("A-D"));
   }

   @Test
   public void testContainingRange() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(66, 67);
      set.unionRange(65, 68);
      assertThat(set.toString(), is("A-D"));
   }

   @Test
   public void testContainsMultiple() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(66, 68);
      set.unionRange(70, 73);
      set.unionRange(76, 78);
      set.unionRange(80, 82);
      set.unionRange(65, 83);
      assertThat(set.toString(), is("A-S"));
   }

   @Test
   public void testEverything() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(0, Integer.MAX_VALUE);
      assertThat(set.toString(), is("..."));
   }

   @Test
   public void testGrowSet() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(66, 68);
      set.unionRange(84, 86);
      set.unionRange(76, 78);
      set.unionRange(70, 73);
      set.unionRange(80, 82);
      set.unionRange(88, 88);
      set.unionRange(90, 90);
      assertThat(set.toString(), is("B-DF-IL-NP-RT-VXZ"));
      set.unionRange(65, 90);
      assertThat(set.toString(), is("A-Z"));
   }

   @Test
   public void testGrowSetInternal() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(90, 90);
      set.unionRange(66, 68);
      set.unionRange(88, 88);
      set.unionRange(80, 82);
      set.unionRange(70, 73);
      set.unionRange(76, 78);
      set.unionRange(84, 86);
      assertThat(set.toString(), is("B-DF-IL-NP-RT-VXZ"));
      set.unionRange(65, 90);
      assertThat(set.toString(), is("A-Z"));
   }

   @Test
   public void testOneChar() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(65, 65);
      assertThat(set.toString(), is("A"));
   }

   @Test
   public void testOneRange() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(65, 68);
      assertThat(set.toString(), is("A-D"));
   }

   @Test
   public void testOneRangeTwice() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(65, 68);
      set.unionRange(65, 68);
      assertThat(set.toString(), is("A-D"));
   }

   @Test
   public void testOverlappedRange() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(66, 68);
      set.unionRange(65, 67);
      assertThat(set.toString(), is("A-D"));
   }

   @Test
   public void testOverlappingRange() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(65, 67);
      set.unionRange(66, 68);
      assertThat(set.toString(), is("A-D"));
   }

   @Test
   public void testOverlapsMultiple() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(65, 68);
      set.unionRange(70, 73);
      set.unionRange(76, 78);
      set.unionRange(80, 82);
      set.unionRange(67, 81);
      assertThat(set.toString(), is("A-R"));
   }

   @Test
   public void testSomeOverrlapping() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(66, 68);
      set.unionRange(70, 72);
      set.unionRange(67, 71);
      assertThat(set.toString(), is("B-H"));
   }

   @Test
   public void testTwoChars() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(65, 65);
      set.unionRange(70, 70);
      assertThat(set.toString(), is("AF"));
   }

   @Test
   public void testTwoCharsBackwards() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(70, 70);
      set.unionRange(65, 65);
      assertThat(set.toString(), is("AF"));
   }

   @Test
   public void testTwoRanges() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(65, 68);
      set.unionRange(70, 73);
      assertThat(set.toString(), is("A-DF-I"));
   }

   @Test
   public void testTwoRangesBackwards() {
      final IntRangeSet set = new IntRangeSet();
      set.unionRange(70, 73);
      set.unionRange(65, 68);
      assertThat(set.toString(), is("A-DF-I"));
   }
}
