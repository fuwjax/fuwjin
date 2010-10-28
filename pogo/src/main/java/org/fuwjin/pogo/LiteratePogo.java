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
package org.fuwjin.pogo;

import org.fuwjin.pogo.parser.CharacterLiteralParser;
import org.fuwjin.pogo.parser.CharacterParser;
import org.fuwjin.pogo.parser.CharacterRangeParser;
import org.fuwjin.pogo.parser.NegativeLookaheadParser;
import org.fuwjin.pogo.parser.OptionParser;
import org.fuwjin.pogo.parser.OptionalParser;
import org.fuwjin.pogo.parser.OptionalSeriesParser;
import org.fuwjin.pogo.parser.PositiveLookaheadParser;
import org.fuwjin.pogo.parser.ReferenceInitAttribute;
import org.fuwjin.pogo.parser.ReferenceMatchAttribute;
import org.fuwjin.pogo.parser.ReferenceResultAttribute;
import org.fuwjin.pogo.parser.RequiredSeriesParser;
import org.fuwjin.pogo.parser.RuleInitAttribute;
import org.fuwjin.pogo.parser.RuleMatchAttribute;
import org.fuwjin.pogo.parser.RuleParser;
import org.fuwjin.pogo.parser.RuleReferenceParser;
import org.fuwjin.pogo.parser.RuleResultAttribute;
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
   public static ParsingExpression and(final ParsingExpression parser) {
      return new PositiveLookaheadParser(parser);
   }

   /**
    * Creates an any character parser.
    * @return the new parser
    */
   public static ParsingExpression dot() {
      return new CharacterParser();
   }

   /**
    * Creates a new Rule Init Attribute.
    * @param name the attribute name
    * @return the new attribute
    */
   public static Attribute init(final String name) {
      return new RuleInitAttribute(name);
   }

   public static Attribute initRef(final String name) {
      return new ReferenceInitAttribute(name);
   }

   /**
    * Creates a literal character parser.
    * @param ch the character to match
    * @return the new parser
    */
   public static ParsingExpression lit(final char ch) {
      return new CharacterLiteralParser(Character.toString(ch));
   }

   /**
    * Creates a new Rule Match Attribute.
    * @param name the attribute name
    * @return the new attribute
    */
   public static Attribute match(final String name) {
      return new RuleMatchAttribute(name);
   }

   public static Attribute matchRef(final String name) {
      if("return".equals(name)) {
         return ReferenceMatchAttribute.RETURN;
      }
      return new ReferenceMatchAttribute(name);
   }

   /**
    * Creates a negative lookahead parser.
    * @param parser the inner parser
    * @return the new parser
    */
   public static ParsingExpression not(final ParsingExpression parser) {
      return new NegativeLookaheadParser(parser);
   }

   /**
    * Creates an option parser.
    * @param parsers the set of options
    * @return the new parser
    */
   public static ParsingExpression option(final ParsingExpression... parsers) {
      final OptionParser p = new OptionParser();
      for(final ParsingExpression parser: parsers) {
         p.add(parser);
      }
      return p.reduce();
   }

   /**
    * Creates an optional parser.
    * @param parser the inner parser
    * @return the new parser
    */
   public static ParsingExpression optional(final ParsingExpression parser) {
      return new OptionalParser(parser);
   }

   /**
    * Creates a required series parser.
    * @param parser the inner parser
    * @return the new parser
    */
   public static ParsingExpression plus(final ParsingExpression parser) {
      return new RequiredSeriesParser(parser);
   }

   /**
    * Creates a new character range parser
    * @param start the smallest allowed character
    * @param end the largest allowed character
    * @return the new parser
    */
   public static ParsingExpression range(final char start, final char end) {
      final CharacterRangeParser peg = new CharacterRangeParser();
      peg.setStart(Character.toString(start));
      peg.setEnd(Character.toString(end));
      return peg;
   }

   public static RuleReferenceParser ref(final String name) {
      return new RuleReferenceParser(name);
   }

   /**
    * Creates a reference parser.
    * @param name the name of the rule to redirect to
    * @param initializer the initalizer before the rule is parsed
    * @param serializer the serializer for the match after the rule is parsed
    * @param finalizer the finalizer after the rule is parsed
    * @return the parser
    */
   public static ParsingExpression ref(final String name, final String initializer, final String serializer, final String finalizer) {
      final RuleReferenceParser ref = ref(name);
      if(!"default".equals(initializer)) {
         ref.add(initRef(initializer));
      }
      if(!"default".equals(serializer)) {
         ref.add(matchRef(serializer));
      }
      if(!"default".equals(finalizer)) {
         ref.add(resultRef(finalizer));
      }
      return ref;
   }

   /**
    * Creates a new Rule Result Attribute.
    * @param name the attribute name
    * @return the new attribute
    */
   public static Attribute result(final String name) {
      return new RuleResultAttribute(name);
   }

   public static Attribute resultRef(final String name) {
      if("return".equals(name)) {
         return ReferenceResultAttribute.RETURN;
      }
      return new ReferenceResultAttribute(name);
   }

   /**
    * Creates a new Rule.
    * @param name the name of the rule
    * @param type the type bound to the rule
    * @param initializer the initializer before the rule is parsed
    * @param serializer the serializer for the match after the rule is parsed
    * @param finalizer the finalizer after the rule is parsed
    * @param parser the expression to parse
    * @return the parser
    */
   public static RuleParser rule(final String name, final Class<?> type, final String initializer,
         final String serializer, final String finalizer, final ParsingExpression parser) {
      return rule(name, type.getCanonicalName(), initializer, serializer, finalizer, parser);
   }

   /**
    * Creates a new Rule.
    * @param name the name of the rule
    * @param type the type bound to the rule
    * @return the parser
    */
   public static RuleParser rule(final String name, final String type) {
      return new RuleParser(name, type);
   }

   /**
    * Creates a new Rule.
    * @param name the name of the rule
    * @param type the type bound to the rule
    * @param initializer the initializer before the rule is parsed
    * @param serializer the serializer for the match after the rule is parsed
    * @param finalizer the finalizer after the rule is parsed
    * @param parser the expression to parse
    * @return the parser
    */
   public static RuleParser rule(final String name, final String type, final String initializer,
         final String serializer, final String finalizer, final ParsingExpression parser) {
      final RuleParser rule = rule(name, type);
      if(!"default".equals(initializer)) {
         rule.add(init(initializer));
      }
      if(!"default".equals(serializer)) {
         rule.add(match(serializer));
      }
      if(!"default".equals(finalizer)) {
         rule.add(result(finalizer));
      }
      rule.expression(parser);
      return rule;
   }

   /**
    * Creates a new sequence parser.
    * @param parsers the inner parsers
    * @return the new parser
    */
   public static ParsingExpression seq(final ParsingExpression... parsers) {
      final SequenceParser p = new SequenceParser();
      for(final ParsingExpression parser: parsers) {
         p.add(parser);
      }
      return p.reduce();
   }

   /**
    * Creates a new optional series parser.
    * @param parser the inner parser
    * @return the new parser
    */
   public static ParsingExpression star(final ParsingExpression parser) {
      return new OptionalSeriesParser(parser);
   }
}
