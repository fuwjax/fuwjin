/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo;

import org.fuwjin.pogo.parser.CharacterLiteralParser;
import org.fuwjin.pogo.parser.CharacterParser;
import org.fuwjin.pogo.parser.CharacterRangeParser;
import org.fuwjin.pogo.parser.NegativeLookaheadParser;
import org.fuwjin.pogo.parser.OptionParser;
import org.fuwjin.pogo.parser.OptionalParser;
import org.fuwjin.pogo.parser.OptionalSeriesParser;
import org.fuwjin.pogo.parser.PositiveLookaheadParser;
import org.fuwjin.pogo.parser.RequiredSeriesParser;
import org.fuwjin.pogo.parser.RuleReferenceParser;
import org.fuwjin.pogo.parser.SequenceParser;

/**
 * The DSL for generating hardcoded Pogo parsers.
 */
public class LiteratePogo { // NO_UCD
   /**
    * Creates a positive lookahead parser.
    * @param parser the inner parser
    * @return the new parser
    */
   public static Parser and(final Parser parser) {
      return new PositiveLookaheadParser(parser);
   }

   /**
    * Creates an any character parser.
    * @return the new parser
    */
   public static Parser dot() {
      return new CharacterParser();
   }

   /**
    * Creates a literal character parser.
    * @param ch the character to match
    * @return the new parser
    */
   public static Parser lit(final char ch) {
      return new CharacterLiteralParser(Character.toString(ch));
   }

   /**
    * Creates a negative lookahead parser.
    * @param parser the inner parser
    * @return the new parser
    */
   public static Parser not(final Parser parser) {
      return new NegativeLookaheadParser(parser);
   }

   /**
    * Creates an option parser.
    * @param parsers the set of options
    * @return the new parser
    */
   public static Parser option(final Parser... parsers) {
      final OptionParser p = new OptionParser();
      for(final Parser parser: parsers) {
         p.add(parser);
      }
      return p.reduce();
   }

   /**
    * Creates an optional parser.
    * @param parser the inner parser
    * @return the new parser
    */
   public static Parser optional(final Parser parser) {
      return new OptionalParser(parser);
   }

   /**
    * Creates a required series parser.
    * @param parser the inner parser
    * @return the new parser
    */
   public static Parser plus(final Parser parser) {
      return new RequiredSeriesParser(parser);
   }

   /**
    * Creates a new character range parser
    * @param start the smallest allowed character
    * @param end the largest allowed character
    * @return the new parser
    */
   public static Parser range(final char start, final char end) {
      final CharacterRangeParser peg = new CharacterRangeParser();
      peg.setStart(Character.toString(start));
      peg.setEnd(Character.toString(end));
      return peg;
   }

   /**
    * Creates a reference parser.
    * @param name the name of the rule to redirect to
    * @param initializer the initalizer before the rule is parsed
    * @param serializer TODO
    * @param finalizer the finalizer after the rule is parsed
    * @return the parser
    */
   public static Parser ref(final String name, final String initializer, final String serializer, final String finalizer) {
      return new RuleReferenceParser(name, initializer, serializer, finalizer);
   }

   /**
    * Creates a new Rule.
    * @param name the name of the rule
    * @param type the type bound to the rule
    * @param initializer the initializer before the rule is parsed
    * @param serializer TODO
    * @param finalizer the finalizer after the rule is parsed
    * @param parser the expression to parse
    * @return the parser
    */
   public static Rule rule(final String name, final Class<?> type, final String initializer, final String serializer,
         final String finalizer, final Parser parser) {
      return new org.fuwjin.pogo.parser.Rule(name, type.getCanonicalName(), initializer, serializer, finalizer, parser);
   }

   public static Rule rule(final String name, final String type, final String initializer, final String serializer,
         final String finalizer, final Parser parser) {
      return new org.fuwjin.pogo.parser.Rule(name, type, initializer, serializer, finalizer, parser);
   }

   /**
    * Creates a new sequence parser.
    * @param parsers the inner parsers
    * @return the new parser
    */
   public static Parser seq(final Parser... parsers) {
      final SequenceParser p = new SequenceParser();
      for(final Parser parser: parsers) {
         p.add(parser);
      }
      return p.reduce();
   }

   /**
    * Creates a new optional series parser.
    * @param parser the inner parser
    * @return the new parser
    */
   public static Parser star(final Parser parser) {
      return new OptionalSeriesParser(parser);
   }
}
