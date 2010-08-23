/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo;

import static org.fuwjin.pogo.LiteratePogo.dot;
import static org.fuwjin.pogo.LiteratePogo.lit;
import static org.fuwjin.pogo.LiteratePogo.not;
import static org.fuwjin.pogo.LiteratePogo.option;
import static org.fuwjin.pogo.LiteratePogo.optional;
import static org.fuwjin.pogo.LiteratePogo.plus;
import static org.fuwjin.pogo.LiteratePogo.range;
import static org.fuwjin.pogo.LiteratePogo.ref;
import static org.fuwjin.pogo.LiteratePogo.rule;
import static org.fuwjin.pogo.LiteratePogo.seq;
import static org.fuwjin.pogo.LiteratePogo.star;

/**
 * The Pogo grammar for parsing pogo grammars.
 */
@SuppressWarnings("nls")
public final class PogoGrammar extends Grammar {
   {
      add(rule(
            "Grammar",
            org.fuwjin.pogo.Grammar.class,
            "new",
            "default",
            "resolve",
            seq(ref("Spacing", "default", "default", "default"), plus(ref("Definition", "default", "default", "add")),
                  ref("EndOfFile", "default", "default", "default"))));
      add(rule(
            "Definition",
            org.fuwjin.pogo.parser.Rule.class,
            "new",
            "default",
            "default",
            seq(ref("Identifier", "default", "default", "name"),
                  optional(ref("TypeInfo", "this", "default", "default")),
                  ref("LEFTARROW", "default", "default", "default"), ref("Expression", "default", "default", "parser"))));
      add(rule(
            "TypeInfo",
            org.fuwjin.pogo.parser.Rule.class,
            "default",
            "default",
            "default",
            seq(ref("EQUALS", "default", "default", "default"),
                  ref("Category", "default", "default", "type"),
                  optional(seq(ref("HASH", "default", "default", "default"),
                        ref("Function", "default", "default", "initializer"))),
                  optional(seq(ref("OUT", "default", "default", "default"),
                        ref("Function", "default", "default", "serializer"))),
                  optional(seq(ref("COLON", "default", "default", "default"),
                        ref("Function", "default", "default", "finalizer"))))));
      add(rule("Category", "default", "default", "default", "default",
            ref("ClassIdentifier", "default", "default", "return")));
      add(rule("Function", "default", "default", "default", "default",
            ref("Identifier", "default", "default", "return")));
      add(rule(
            "Expression",
            org.fuwjin.pogo.parser.OptionParser.class,
            "new",
            "default",
            "reduce",
            seq(ref("Sequence", "default", "default", "add"),
                  star(seq(ref("SLASH", "default", "default", "default"), ref("Sequence", "default", "default", "add"))))));
      add(rule("Sequence", org.fuwjin.pogo.parser.SequenceParser.class, "new", "default", "reduce",
            plus(ref("Prefix", "default", "default", "add"))));
      add(rule(
            "Prefix",
            "default",
            "default",
            "default",
            "default",
            option(ref("AND", "default", "default", "return"), ref("NOT", "default", "default", "return"),
                  ref("Suffix", "default", "default", "return"))));
      add(rule(
            "Suffix",
            "default",
            "default",
            "default",
            "default",
            option(ref("QUESTION", "default", "default", "return"), ref("STAR", "default", "default", "return"),
                  ref("PLUS", "default", "default", "return"), ref("Primary", "default", "default", "return"))));
      add(rule(
            "Primary",
            "default",
            "default",
            "default",
            "default",
            option(
                  ref("Reference", "default", "default", "return"),
                  seq(ref("OPEN", "default", "default", "default"), ref("Expression", "default", "default", "return"),
                        ref("CLOSE", "default", "default", "default")), ref("Literal", "default", "default", "return"),
                  ref("CharClass", "default", "default", "return"), ref("DOT", "default", "default", "return"))));
      add(rule(
            "Reference",
            org.fuwjin.pogo.parser.RuleReferenceParser.class,
            "new",
            "default",
            "default",
            seq(ref("Identifier", "default", "default", "ruleName"),
                  not(option(lit('<'), lit('='))),
                  optional(seq(ref("HASH", "default", "default", "default"),
                        ref("Function", "default", "default", "constructor"))),
                  optional(seq(ref("OUT", "default", "default", "default"),
                        ref("Function", "default", "default", "matcher"))),
                  optional(seq(ref("COLON", "default", "default", "default"),
                        ref("Function", "default", "default", "converter"))))));
      add(rule(
            "Literal",
            org.fuwjin.pogo.parser.SequenceParser.class,
            "new",
            "default",
            "reduce",
            option(
                  seq(lit('\''), star(seq(not(lit('\'')), ref("LitChar", "default", "default", "add"))), lit('\''),
                        ref("Spacing", "default", "default", "default")),
                  seq(lit('"'), star(seq(not(lit('"')), ref("LitChar", "default", "default", "add"))), lit('"'),
                        ref("Spacing", "default", "default", "default")))));
      add(rule(
            "CharClass",
            org.fuwjin.pogo.parser.OptionParser.class,
            "new",
            "default",
            "reduce",
            seq(lit('['),
                  star(seq(not(lit(']')),
                        option(ref("Range", "default", "default", "add"), ref("LitChar", "default", "default", "add")))),
                  lit(']'), ref("Spacing", "default", "default", "default"))));
      add(rule("Range", org.fuwjin.pogo.parser.CharacterRangeParser.class, "new", "default", "default",
            seq(ref("Char", "default", "default", "setStart"), lit('-'), ref("Char", "default", "default", "setEnd"))));
      add(rule("LitChar", org.fuwjin.pogo.parser.CharacterLiteralParser.class, "new", "default", "default",
            ref("Char", "default", "default", "set")));
      add(rule(
            "Char",
            "default",
            "default",
            "default",
            "default",
            option(seq(lit('\\'), ref("EscapeChar", "default", "default", "return")),
                  ref("PlainChar", "default", "return", "default"))));
      add(rule("PlainChar", "default", "default", "default", "default", seq(not(lit('\\')), dot())));
      add(rule(
            "EscapeChar",
            "default",
            "default",
            "default",
            "default",
            option(ref("OperatorChar", "default", "return", "default"),
                  ref("ControlChar", "default", "default", "return"), ref("OctalChar", "default", "default", "return"),
                  seq(lit('x'), ref("UnicodeChar", "default", "default", "return")))));
      add(rule("OperatorChar", "default", "default", "default", "default",
            option(lit('-'), lit('\''), lit('"'), lit('['), lit(']'), lit('\\'))));
      add(rule("ClassIdentifier", "default", "default", "default", "default",
            seq(ref("ClassIdent", "default", "return", "default"), ref("Spacing", "default", "default", "default"))));
      add(rule("Identifier", "default", "default", "default", "default",
            seq(ref("Ident", "default", "return", "default"), ref("Spacing", "default", "default", "default"))));
      add(rule(
            "ClassIdent",
            "default",
            "default",
            "default",
            "default",
            seq(ref("Ident", "default", "default", "default"),
                  star(seq(option(lit('.'), lit('$')), ref("Ident", "default", "default", "default"))))));
      add(rule(
            "Ident",
            "default",
            "default",
            "default",
            "default",
            seq(ref("IdentStart", "default", "default", "default"),
                  star(ref("IdentCont", "default", "default", "default")))));
      add(rule("IdentCont", "default", "default", "default", "default",
            option(ref("IdentStart", "default", "default", "default"), range('0', '9'))));
      add(rule("IdentStart", "default", "default", "default", "default",
            option(range('a', 'z'), range('A', 'Z'), lit('_'))));
      add(rule("ControlChar", org.fuwjin.pogo.parser.CharacterLiteralParser.class, "default", "slash", "default",
            option(lit('n'), lit('r'), lit('t'))));
      add(rule(
            "OctalChar",
            org.fuwjin.pogo.parser.CharacterLiteralParser.class,
            "default",
            "octal",
            "default",
            option(seq(range('0', '3'), range('0', '7'), range('0', '7')),
                  seq(range('0', '7'), optional(range('0', '7'))))));
      add(rule("UnicodeChar", org.fuwjin.pogo.parser.CharacterLiteralParser.class, "default", "unicode", "default",
            plus(option(range('0', '9'), range('A', 'F'), range('a', 'f')))));
      add(rule("LEFTARROW", "default", "default", "default", "default",
            seq(seq(lit('<'), lit('-')), ref("Spacing", "default", "default", "default"))));
      add(rule("EQUALS", "default", "default", "default", "default",
            seq(lit('='), ref("Spacing", "default", "default", "default"))));
      add(rule("HASH", "default", "default", "default", "default",
            seq(lit('~'), ref("Spacing", "default", "default", "default"))));
      add(rule("OUT", "default", "default", "default", "default",
            seq(lit('>'), ref("Spacing", "default", "default", "default"))));
      add(rule("COLON", "default", "default", "default", "default",
            seq(lit(':'), ref("Spacing", "default", "default", "default"))));
      add(rule("SLASH", "default", "default", "default", "default",
            seq(lit('/'), ref("Spacing", "default", "default", "default"))));
      add(rule(
            "AND",
            org.fuwjin.pogo.parser.PositiveLookaheadParser.class,
            "new",
            "default",
            "default",
            seq(lit('&'), ref("Spacing", "default", "default", "default"),
                  ref("Suffix", "default", "default", "parser"))));
      add(rule(
            "NOT",
            org.fuwjin.pogo.parser.NegativeLookaheadParser.class,
            "new",
            "default",
            "default",
            seq(lit('!'), ref("Spacing", "default", "default", "default"),
                  ref("Suffix", "default", "default", "parser"))));
      add(rule(
            "QUESTION",
            org.fuwjin.pogo.parser.OptionalParser.class,
            "new",
            "default",
            "default",
            seq(ref("Primary", "default", "default", "parser"), lit('?'),
                  ref("Spacing", "default", "default", "default"))));
      add(rule(
            "STAR",
            org.fuwjin.pogo.parser.OptionalSeriesParser.class,
            "new",
            "default",
            "default",
            seq(ref("Primary", "default", "default", "parser"), lit('*'),
                  ref("Spacing", "default", "default", "default"))));
      add(rule(
            "PLUS",
            org.fuwjin.pogo.parser.RequiredSeriesParser.class,
            "new",
            "default",
            "default",
            seq(ref("Primary", "default", "default", "parser"), lit('+'),
                  ref("Spacing", "default", "default", "default"))));
      add(rule("OPEN", "default", "default", "default", "default",
            seq(lit('('), ref("Spacing", "default", "default", "default"))));
      add(rule("CLOSE", "default", "default", "default", "default",
            seq(lit(')'), ref("Spacing", "default", "default", "default"))));
      add(rule("DOT", org.fuwjin.pogo.parser.CharacterParser.class, "new", "default", "default",
            seq(lit('.'), ref("Spacing", "default", "default", "default"))));
      add(rule(
            "Spacing",
            "default",
            "default",
            "default",
            "default",
            star(option(ref("Space", "default", "default", "default"), ref("Comment", "default", "default", "default")))));
      add(rule(
            "Comment",
            "default",
            "default",
            "default",
            "default",
            seq(lit('#'), star(seq(not(ref("EndOfLine", "default", "default", "default")), dot())),
                  ref("EndOfLine", "default", "default", "default"))));
      add(rule("Space", "default", "default", "default", "default",
            option(lit(' '), lit('\t'), ref("EndOfLine", "default", "default", "default"))));
      add(rule("EndOfLine", "default", "default", "default", "default",
            option(seq(lit('\r'), lit('\n')), option(lit('\r'), lit('\n')))));
      add(rule("EndOfFile", "default", "default", "default", "default", not(dot())));
      resolve();
   }
   private static final Grammar grammar = new PogoGrammar();

   /**
    * Creates a new Pogo parser from the Pogo formatted input.
    * @param reader the input
    * @return the new parser
    * @throws PogoException
    */
   public static Grammar readGrammar(final CodePointStream stream) throws PogoException {
      return (Grammar)grammar.parse(stream);
   }

   /**
    * Return the grammar for parsing pogo files.
    * @return the grammar
    */
   public static Grammar staticPogoGrammar() {
      return grammar;
   }
}
