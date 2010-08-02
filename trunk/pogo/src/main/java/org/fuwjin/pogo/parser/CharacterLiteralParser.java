/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.parser;

import static org.fuwjin.pogo.parser.PrintLevel.LITERAL_OPTION;
import static org.fuwjin.pogo.parser.PrintLevel.LITERAL_SEQUENCE;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.util.Map;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Pogo;
import org.fuwjin.pogo.PogoGrammar;
import org.fuwjin.pogo.reflect.ReflectionType;

/**
 * Matches a single specified character from the input.
 */
public class CharacterLiteralParser implements Parser {
   private static final String SINGLE_LIT = "SingleLit"; //$NON-NLS-1$

   /**
    * Creates a unicode code point from an octal literal.
    * @param ch the octal code
    * @return the unicode code point
    */
   protected static int octal(final String ch) {
      return Integer.valueOf(ch, 8);
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
   private static Pogo serial;

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
      return LITERAL_OPTION.escape((char)ch);
   }

   /**
    * Returns the value for a literal class.
    * @return the value
    */
   protected String getLitChar() {
      return LITERAL_SEQUENCE.escape((char)ch);
   }

   @Override
   public int hashCode() {
      return hash(getClass(), ch);
   }

   @Override
   public void parse(final PogoContext context) {
      context.accept(ch);
   }

   @Override
   public void resolve(final Map<String, Rule> grammar, final ReflectionType ruleType) {
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
      if(serial == null) {
         serial = PogoGrammar.pogoParseGrammar().get(SINGLE_LIT);
      }
      return serial.serial(this);
   }
}
