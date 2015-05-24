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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjin.util.CodePointSet;
import org.junit.Test;

/**
 * Tests the code point set.
 */
public class CodePointSetTest {
   /**
    * Tests the union of adjacent characters to a range.
    */
   @Test
   public void testAdjacentChars() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(66, 72);
      set.unionRange(65, 65);
      set.unionRange(73, 73);
      assertThat(set.toString(), is("A-I"));
   }

   /**
    * Tests the union of adjacent ranges.
    */
   @Test
   public void testAdjacentRange() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(69, 72);
      set.unionRange(66, 68);
      set.unionRange(73, 75);
      assertThat(set.toString(), is("B-K"));
   }

   /**
    * Tests the union of the capitals one at a time.
    */
   @Test
   public void testAlpha() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(65, 65);
      for(char i = 66; i < 91; i++) {
         set.unionRange(i, i);
         assertThat(set.toString(), is("A-" + i));
      }
   }

   /**
    * Tests the union of the capitals five at a time.
    */
   @Test
   public void testAlphaRange() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(65, 65);
      for(char i = 70; i < 91; i += 5) {
         set.unionRange(i - 4, i);
         assertThat(set.toString(), is("A-" + i));
      }
   }

   /**
    * Tests the union of a range contained in another.
    */
   @Test
   public void testContainedRange() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(65, 68);
      set.unionRange(66, 67);
      assertThat(set.toString(), is("A-D"));
   }

   /**
    * Tests the union of a range containing another.
    */
   @Test
   public void testContainingRange() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(66, 67);
      set.unionRange(65, 68);
      assertThat(set.toString(), is("A-D"));
   }

   /**
    * Tests the union of a range containing many ranges.
    */
   @Test
   public void testContainsMultiple() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(66, 68);
      set.unionRange(70, 73);
      set.unionRange(76, 78);
      set.unionRange(80, 82);
      set.unionRange(65, 83);
      assertThat(set.toString(), is("A-S"));
   }

   /**
    * Tests the range of all positive integers.
    */
   @Test
   public void testEverything() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(0, Integer.MAX_VALUE);
      assertThat(set.toString(), is("..."));
   }

   /**
    * Tests that more ranges than the initial set size can be added.
    */
   @Test
   public void testGrowSet() {
      final CodePointSet set = new CodePointSet();
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

   /**
    * Tests that ranges can be added to the middle of the set.
    */
   @Test
   public void testGrowSetInternal() {
      final CodePointSet set = new CodePointSet();
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

   /**
    * Tests a single char range.
    */
   @Test
   public void testOneChar() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(65, 65);
      assertThat(set.toString(), is("A"));
   }

   /**
    * Tests a single range.
    */
   @Test
   public void testOneRange() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(65, 68);
      assertThat(set.toString(), is("A-D"));
   }

   /**
    * Tests a repeated range.
    */
   @Test
   public void testOneRangeTwice() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(65, 68);
      set.unionRange(65, 68);
      assertThat(set.toString(), is("A-D"));
   }

   /**
    * Tests overlapped ranges.
    */
   @Test
   public void testOverlappedRange() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(66, 68);
      set.unionRange(65, 67);
      assertThat(set.toString(), is("A-D"));
   }

   /**
    * Tests overlapping ranges.
    */
   @Test
   public void testOverlappingRange() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(65, 67);
      set.unionRange(66, 68);
      assertThat(set.toString(), is("A-D"));
   }

   /**
    * Tests multiple overlapping ranges.
    */
   @Test
   public void testOverlapsMultiple() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(65, 68);
      set.unionRange(70, 73);
      set.unionRange(76, 78);
      set.unionRange(80, 82);
      set.unionRange(67, 81);
      assertThat(set.toString(), is("A-R"));
   }

   /**
    * Tests a few overlapping ranges.
    */
   @Test
   public void testSomeOverrlapping() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(66, 68);
      set.unionRange(70, 72);
      set.unionRange(67, 71);
      assertThat(set.toString(), is("B-H"));
   }

   /**
    * Test two characters.
    */
   @Test
   public void testTwoChars() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(65, 65);
      set.unionRange(70, 70);
      assertThat(set.toString(), is("AF"));
   }

   /**
    * Test two characters in reverse.
    */
   @Test
   public void testTwoCharsBackwards() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(70, 70);
      set.unionRange(65, 65);
      assertThat(set.toString(), is("AF"));
   }

   /**
    * Test two ranges.
    */
   @Test
   public void testTwoRanges() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(65, 68);
      set.unionRange(70, 73);
      assertThat(set.toString(), is("A-DF-I"));
   }

   /**
    * Test two ranges in reverse.
    */
   @Test
   public void testTwoRangesBackwards() {
      final CodePointSet set = new CodePointSet();
      set.unionRange(70, 73);
      set.unionRange(65, 68);
      assertThat(set.toString(), is("A-DF-I"));
   }
}
