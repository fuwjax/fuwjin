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
package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Rule;
import org.fuwjin.pogo.state.PogoState;

/**
 * Matches a single specified character from the input.
 */
public class CharacterLiteralParser implements Parser {
   private static String getCh(final int ch, final String dirty, final String escaped) {
      final int index = dirty.indexOf(ch);
      if(index >= 0) {
         return new String(new char[]{'\\', escaped.charAt(index)});
      }
      return new String(Character.toChars(ch));
   }

   /**
    * Creates a unicode code point from an octal literal.
    * @param ch the octal code
    * @return the unicode code point
    */
   protected static int octal(final String ch) {
      return Integer.valueOf(ch, 8);
   }

   /**
    * Creates a unicode code point from a control character.
    * @param ch the control character
    * @return the unicode code point
    */
   protected static int slash(final String ch) {
      final int index = "nrt".indexOf(ch); //$NON-NLS-1$
      if(index >= 0) {
         return "\n\r\t".charAt(index); //$NON-NLS-1$
      }
      return ch.charAt(0);
   }

   /**
    * Creates a unicode code point from a hex literal.
    * @param ch the hex code
    * @return the unicode code point
    */
   protected static int unicode(final String ch) {
      return Integer.valueOf(ch, 16);
   }

   private int ch;

   /**
    * Creates a new instance.
    */
   CharacterLiteralParser() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param ch the literal
    */
   public CharacterLiteralParser(final String ch) {
      set(ch);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final CharacterLiteralParser o = (CharacterLiteralParser)obj;
         return eq(getClass(), o.getClass()) && eq(ch, o.ch);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   /**
    * Returns the value for a character class.
    * @return the value
    */
   protected String getClassChar() {
      return getCh(ch, "\n\r\t\\[]-", "nrt\\[]-");
   }

   /**
    * Returns the value for a literal class.
    * @return the value
    */
   protected String getLitChar() {
      return getCh(ch, "\n\r\t\\'", "nrt\\'");
   }

   @Override
   public int hashCode() {
      return hash(getClass(), ch);
   }

   @Override
   public boolean parse(final PogoState state) {
      return state.advance(ch, ch);
   }

   @Override
   public void resolve(final Grammar grammar, final Rule parent) {
      // do nothing
   }

   /**
    * Sets the character matched by this parser to ch.
    * @param ch the character to match
    */
   public void set(final int ch) {
      this.ch = ch;
   }

   /**
    * Sets the character matched by this parser to ch.
    * @param ch the character to match
    */
   public void set(final String ch) {
      this.ch = ch.charAt(0);
   }

   @Override
   public String toString() {
      return '"' + new String(Character.toChars(ch)) + '"';
   }
}
