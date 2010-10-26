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

import static org.fuwjin.pogo.LiteratePogo.and;
import static org.fuwjin.pogo.LiteratePogo.dot;
import static org.fuwjin.pogo.LiteratePogo.init;
import static org.fuwjin.pogo.LiteratePogo.lit;
import static org.fuwjin.pogo.LiteratePogo.match;
import static org.fuwjin.pogo.LiteratePogo.not;
import static org.fuwjin.pogo.LiteratePogo.option;
import static org.fuwjin.pogo.LiteratePogo.optional;
import static org.fuwjin.pogo.LiteratePogo.plus;
import static org.fuwjin.pogo.LiteratePogo.range;
import static org.fuwjin.pogo.LiteratePogo.ref;
import static org.fuwjin.pogo.LiteratePogo.result;
import static org.fuwjin.pogo.LiteratePogo.rule;
import static org.fuwjin.pogo.LiteratePogo.seq;
import static org.fuwjin.pogo.LiteratePogo.star;

/**
 * The Pogo grammar for parsing pogo grammars.
 */
@SuppressWarnings("nls")
public final class PogoGrammar extends Grammar {
   {
      add(rule("Grammar", "org.fuwjin.pogo.Grammar").add(init("new")).add(result("resolve")).expression(
            seq(ref("Spacing", "default", "default", "default"), plus(ref("Rule", "default", "default", "add")), ref(
                  "EndOfFile", "default", "default", "default"))));
      add(rule("Rule", "org.fuwjin.pogo.parser.RuleParser").expression(
            seq(ref("RuleIdent", "default", "default", "return"), optional(ref("RuleAttributes", "this", "default",
                  "default")), ref("LEFTARROW", "default", "default", "default"), ref("Expression", "default",
                  "default", "parser"))));
      add(rule("RuleIdent", "org.fuwjin.pogo.parser.RuleParser").add(result("new")).expression(
            seq(ref("Identifier", "default", "default", "return"), and(option(lit('='), lit('<'))))));
      add(rule("RuleAttributes", "org.fuwjin.pogo.parser.RuleParser").expression(
            seq(ref("EQUALS", "default", "default", "default"), ref("Namespace", "default", "default", "namespace"),
                  optional(ref("RuleInit", "default", "default", "add")), optional(ref("RuleMatch", "default",
                        "default", "add")), optional(ref("RuleResult", "default", "default", "add")))));
      add(rule("RuleInit", "org.fuwjin.pogo.parser.RuleInitAttribute").add(result("new")).expression(
            seq(ref("HASH", "default", "default", "default"), ref("Identifier", "default", "default", "return"))));
      add(rule("RuleMatch", "org.fuwjin.pogo.parser.RuleMatchAttribute").add(result("new")).expression(
            seq(ref("OUT", "default", "default", "default"), ref("Identifier", "default", "default", "return"))));
      add(rule("RuleResult", "org.fuwjin.pogo.parser.RuleResultAttribute").add(result("new")).expression(
            seq(ref("COLON", "default", "default", "default"), ref("Identifier", "default", "default", "return"))));
      add(rule("Namespace", "default")
            .expression(
                  seq(ref("NamespaceIdent", "default", "return", "default"), ref("Spacing", "default", "default",
                        "default"))));
      add(rule("Function", "org.fuwjin.postage.CompositeFunction").add(result("new")).expression(
            ref("Identifier", "default", "default", "return")));
      add(rule("Expression", "org.fuwjin.pogo.parser.OptionParser").add(init("new")).add(result("reduce")).expression(
            seq(ref("Sequence", "default", "default", "add"), star(seq(ref("SLASH", "default", "default", "default"),
                  ref("Sequence", "default", "default", "add"))))));
      add(rule("Sequence", "org.fuwjin.pogo.parser.SequenceParser").add(init("new")).add(result("reduce")).expression(
            plus(ref("Prefix", "default", "default", "add"))));
      add(rule("Prefix", "default").expression(
            option(ref("AND", "default", "default", "return"), ref("NOT", "default", "default", "return"), ref(
                  "Suffix", "default", "default", "return"))));
      add(rule("Suffix", "default").expression(
            option(ref("QUESTION", "default", "default", "return"), ref("STAR", "default", "default", "return"), ref(
                  "PLUS", "default", "default", "return"), ref("Primary", "default", "default", "return"))));
      add(rule("Primary", "default").expression(
            option(ref("Reference", "default", "default", "return"), seq(ref("OPEN", "default", "default", "default"),
                  ref("Expression", "default", "default", "return"), ref("CLOSE", "default", "default", "default")),
                  ref("Literal", "default", "default", "return"), ref("CharClass", "default", "default", "return"),
                  ref("DOT", "default", "default", "return"))));
      add(rule("Reference", "org.fuwjin.pogo.parser.RuleReferenceParser").add(init("new")).expression(
            seq(ref("Identifier", "default", "default", "ruleName"), not(option(lit('<'), lit('='))), optional(seq(ref(
                  "HASH", "default", "default", "default"), ref("Function", "default", "default", "constructor"))),
                  optional(seq(ref("OUT", "default", "default", "default"), ref("Function", "default", "default",
                        "matcher"))), optional(seq(ref("COLON", "default", "default", "default"), ref("Function",
                        "default", "default", "converter"))))));
      add(rule("Literal", "org.fuwjin.pogo.parser.SequenceParser").add(init("new")).add(result("reduce"))
            .expression(
                  option(seq(lit('\''), star(seq(not(lit('\'')), ref("LitChar", "default", "default", "add"))),
                        lit('\''), ref("Spacing", "default", "default", "default")), seq(lit('"'), star(seq(
                        not(lit('"')), ref("LitChar", "default", "default", "add"))), lit('"'), ref("Spacing",
                        "default", "default", "default")))));
      add(rule("CharClass", "org.fuwjin.pogo.parser.OptionParser").add(init("new")).add(result("reduce")).expression(
            seq(lit('['), star(seq(not(lit(']')), option(ref("Range", "default", "default", "add"), ref("LitChar",
                  "default", "default", "add")))), lit(']'), ref("Spacing", "default", "default", "default"))));
      add(rule("Range", "org.fuwjin.pogo.parser.CharacterRangeParser").add(init("new")).expression(
            seq(ref("Char", "default", "default", "setStart"), lit('-'), ref("Char", "default", "default", "setEnd"))));
      add(rule("LitChar", "org.fuwjin.pogo.parser.CharacterLiteralParser").add(init("new")).expression(
            ref("Char", "default", "default", "set")));
      add(rule("Char", "default").expression(
            option(seq(lit('\\'), ref("EscapeChar", "default", "default", "return")), ref("PlainChar", "default",
                  "return", "default"))));
      add(rule("PlainChar", "default").expression(seq(not(lit('\\')), dot())));
      add(rule("EscapeChar", "default").expression(
            option(ref("Operator", "default", "return", "default"), ref("ControlChar", "default", "default", "return"),
                  ref("OctalChar", "default", "default", "return"), seq(lit('x'), ref("UnicodeChar", "default",
                        "default", "return")))));
      add(rule("Operator", "default").expression(option(lit('-'), lit('\''), lit('"'), lit('['), lit(']'), lit('\\'))));
      add(rule("Identifier", "default").expression(
            seq(ref("Ident", "default", "return", "default"), ref("Spacing", "default", "default", "default"))));
      add(rule("NamespaceIdent", "default").expression(
            star(seq(not(option(lit('~'), lit('>'), lit(':'), lit('<'), lit(' '), lit('\n'), lit('\r'), lit('\t'))),
                  dot()))));
      add(rule("Ident", "default").expression(
            seq(ref("IdentStart", "default", "default", "default"), star(ref("IdentCont", "default", "default",
                  "default")))));
      add(rule("IdentCont", "default").expression(
            option(ref("IdentStart", "default", "default", "default"), range('0', '9'))));
      add(rule("IdentStart", "default").expression(option(range('a', 'z'), range('A', 'Z'), lit('_'))));
      add(rule("ControlChar", "org.fuwjin.pogo.parser.CharacterLiteralParser").add(match("slash")).expression(
            option(lit('n'), lit('r'), lit('t'))));
      add(rule("OctalChar", "org.fuwjin.pogo.parser.CharacterLiteralParser").add(match("octal")).expression(
            option(seq(range('0', '3'), range('0', '7'), range('0', '7')), seq(range('0', '7'),
                  optional(range('0', '7'))))));
      add(rule("UnicodeChar", "org.fuwjin.pogo.parser.CharacterLiteralParser").add(match("unicode")).expression(
            plus(option(range('0', '9'), range('A', 'F'), range('a', 'f')))));
      add(rule("LEFTARROW", "default").expression(
            seq(seq(lit('<'), lit('-')), ref("Spacing", "default", "default", "default"))));
      add(rule("EQUALS", "default").expression(seq(lit('='), ref("Spacing", "default", "default", "default"))));
      add(rule("HASH", "default").expression(seq(lit('~'), ref("Spacing", "default", "default", "default"))));
      add(rule("OUT", "default").expression(seq(lit('>'), ref("Spacing", "default", "default", "default"))));
      add(rule("COLON", "default").expression(seq(lit(':'), ref("Spacing", "default", "default", "default"))));
      add(rule("AND", "org.fuwjin.pogo.parser.PositiveLookaheadParser").add(init("new")).expression(
            seq(lit('&'), ref("Spacing", "default", "default", "default"),
                  ref("Suffix", "default", "default", "parser"))));
      add(rule("NOT", "org.fuwjin.pogo.parser.NegativeLookaheadParser").add(init("new")).expression(
            seq(lit('!'), ref("Spacing", "default", "default", "default"),
                  ref("Suffix", "default", "default", "parser"))));
      add(rule("QUESTION", "org.fuwjin.pogo.parser.OptionalParser").add(init("new")).expression(
            seq(ref("Primary", "default", "default", "parser"), lit('?'), ref("Spacing", "default", "default",
                  "default"))));
      add(rule("STAR", "org.fuwjin.pogo.parser.OptionalSeriesParser").add(init("new")).expression(
            seq(ref("Primary", "default", "default", "parser"), lit('*'), ref("Spacing", "default", "default",
                  "default"))));
      add(rule("PLUS", "org.fuwjin.pogo.parser.RequiredSeriesParser").add(init("new")).expression(
            seq(ref("Primary", "default", "default", "parser"), lit('+'), ref("Spacing", "default", "default",
                  "default"))));
      add(rule("OPEN", "default").expression(seq(lit('('), ref("Spacing", "default", "default", "default"))));
      add(rule("CLOSE", "default").expression(seq(lit(')'), ref("Spacing", "default", "default", "default"))));
      add(rule("SLASH", "default").expression(seq(lit('/'), ref("Spacing", "default", "default", "default"))));
      add(rule("DOT", "org.fuwjin.pogo.parser.CharacterParser").add(init("new")).expression(
            seq(lit('.'), ref("Spacing", "default", "default", "default"))));
      add(rule("Spacing", "default")
            .expression(
                  star(option(ref("Space", "default", "default", "default"), ref("Comment", "default", "default",
                        "default")))));
      add(rule("Comment", "default").expression(
            seq(lit('#'), star(seq(not(ref("EndOfLine", "default", "default", "default")), dot())), ref("EndOfLine",
                  "default", "default", "default"))));
      add(rule("Space", "default").expression(
            option(lit(' '), lit('\t'), ref("EndOfLine", "default", "default", "default"))));
      add(rule("EndOfLine", "default").expression(option(seq(lit('\r'), lit('\n')), option(lit('\r'), lit('\n')))));
      add(rule("EndOfFile", "default").expression(not(dot())));
      resolve();
   }
   private static final Grammar grammar = new PogoGrammar();

   /**
    * Return the grammar for parsing pogo files.
    * @return the grammar
    */
   public static Grammar staticPogoGrammar() {
      return grammar;
   }
}
