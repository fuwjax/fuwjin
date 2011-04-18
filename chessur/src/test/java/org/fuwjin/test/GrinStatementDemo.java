/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.test;

import static org.fuwjin.chessur.Catalog.loadCat;
import static org.fuwjin.chessur.InStream.streamOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.ChessurInterpreter.ChessurException;
import org.junit.Test;

/**
 * Demos highlighting each type of Grin Statement.
 */
public class GrinStatementDemo {
   /**
    * Demo an "either or" statement.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoAssignment() throws ChessurException {
      final Catalog parser = loadCat("<Root>{either { accept 'a' v = 'yes' } or v = 'no' return v}");
      assertThat((String)parser.transform(streamOf("ab")), is("yes"));
      assertThat((String)parser.transform(streamOf("aabc")), is("yes"));
      assertThat((String)parser.transform(streamOf("c")), is("no"));
      assertThat((String)parser.transform(streamOf("")), is("no"));
   }

   /**
    * Demo a "could" statement.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoCould() throws ChessurException {
      final Catalog parser = loadCat("<Root>{could accept next return match}");
      assertThat((String)parser.transform(streamOf("ab")), is("a"));
      assertThat((String)parser.transform(streamOf("aabc")), is("a"));
      assertThat((String)parser.transform(streamOf("")), is(""));
   }

   /**
    * Demo an "either or" statement.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoEitherOr() throws ChessurException {
      final Catalog parser = loadCat("<Root>{either accept 'a' or accept 'c' return match}");
      assertThat((String)parser.transform(streamOf("ab")), is("a"));
      assertThat((String)parser.transform(streamOf("aabc")), is("a"));
      assertThat((String)parser.transform(streamOf("c")), is("c"));
      try {
         parser.transform(streamOf("bc"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("unexpected"));
      }
      try {
         parser.transform(streamOf(""));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("unexpected"));
      }
   }

   /**
    * Demo an "either or or" statement.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoEitherOrOr() throws ChessurException {
      final Catalog parser = loadCat("<Root>{either accept 'a' or accept 'c' or accept 'e' return match}");
      assertThat((String)parser.transform(streamOf("ab")), is("a"));
      assertThat((String)parser.transform(streamOf("eabc")), is("e"));
      assertThat((String)parser.transform(streamOf("c")), is("c"));
      try {
         parser.transform(streamOf("bc"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("unexpected"));
      }
      try {
         parser.transform(streamOf(""));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("unexpected"));
      }
   }

   /**
    * Demo an "is in [range]" statement.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoIsIn() throws ChessurException {
      final Catalog parser = loadCat("<Root>{is in a,b,c return 'yes'}");
      assertThat((String)parser.transform(streamOf("a")), is("yes"));
      assertThat((String)parser.transform(streamOf("bc")), is("yes"));
      try {
         parser.transform(streamOf(""));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("EOF"));
      }
      try {
         parser.transform(streamOf("d"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("not match filter"));
      }
      try {
         parser.transform(streamOf("dbca"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("not match filter"));
      }
   }

   /**
    * Demo an "is 'literal'" statement.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoIsLiteral() throws ChessurException {
      final Catalog parser = loadCat("<Root>{is 'abc' return 'yes'}");
      assertThat((String)parser.transform(streamOf("abc")), is("yes"));
      assertThat((String)parser.transform(streamOf("abcd")), is("yes"));
      try {
         parser.transform(streamOf(""));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("failed while matching"));
      }
      try {
         parser.transform(streamOf("ab"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("failed while matching"));
      }
      try {
         parser.transform(streamOf("aabc"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("failed while matching"));
      }
   }

   /**
    * Demo an "is not accept" statement.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoIsNotAccept() throws ChessurException {
      final Catalog parser = loadCat("<Root>{is not accept next return 'yes'}");
      assertThat((String)parser.transform(streamOf("")), is("yes"));
      try {
         parser.transform(streamOf("a"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("unexpected"));
      }
   }

   /**
    * Demo an "is not 'literal'" statement.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoIsNotLiteral() throws ChessurException {
      final Catalog parser = loadCat("<Root>{is not 'abc' return 'yes'}");
      assertThat((String)parser.transform(streamOf("ab")), is("yes"));
      assertThat((String)parser.transform(streamOf("aabc")), is("yes"));
      assertThat((String)parser.transform(streamOf("")), is("yes"));
      try {
         parser.transform(streamOf("abc"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("unexpected"));
      }
      try {
         parser.transform(streamOf("abcd"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("unexpected"));
      }
   }

   /**
    * Demo a "repeat" statement.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoRepeat() throws ChessurException {
      final Catalog parser = loadCat("<Root>{repeat accept in a-z return match}");
      assertThat((String)parser.transform(streamOf("ab")), is("ab"));
      assertThat((String)parser.transform(streamOf("aaBC")), is("aa"));
      assertThat((String)parser.transform(streamOf("c")), is("c"));
      try {
         parser.transform(streamOf("BC"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("not match filter"));
      }
      try {
         parser.transform(streamOf(""));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("EOF"));
      }
   }
}
