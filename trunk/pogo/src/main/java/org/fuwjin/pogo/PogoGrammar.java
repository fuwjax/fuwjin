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
import static org.fuwjin.pogo.LiteratePogo.initRef;
import static org.fuwjin.pogo.LiteratePogo.lit;
import static org.fuwjin.pogo.LiteratePogo.match;
import static org.fuwjin.pogo.LiteratePogo.matchRef;
import static org.fuwjin.pogo.LiteratePogo.not;
import static org.fuwjin.pogo.LiteratePogo.option;
import static org.fuwjin.pogo.LiteratePogo.optional;
import static org.fuwjin.pogo.LiteratePogo.plus;
import static org.fuwjin.pogo.LiteratePogo.range;
import static org.fuwjin.pogo.LiteratePogo.ref;
import static org.fuwjin.pogo.LiteratePogo.result;
import static org.fuwjin.pogo.LiteratePogo.resultRef;
import static org.fuwjin.pogo.LiteratePogo.rule;
import static org.fuwjin.pogo.LiteratePogo.seq;
import static org.fuwjin.pogo.LiteratePogo.star;

/**
 * The Pogo grammar for parsing pogo grammars.
 */
@SuppressWarnings("nls")
public final class PogoGrammar extends Grammar {
   {
      try {
         add(rule("Grammar", "org.fuwjin.pogo.Grammar").add(init("new")).add(result("resolve")).expression(
               seq(ref("Spacing"), plus(ref("Rule").add(resultRef("add"))), ref("EndOfFile"))));
         add(rule("Rule", "org.fuwjin.pogo.parser.RuleParser").expression(
               seq(ref("RuleIdent").add(resultRef("return")), optional(ref("RuleAttributes").add(initRef("this"))),
                     ref("LEFTARROW"), ref("Expression").add(resultRef("parser")))));
         add(rule("RuleIdent", "org.fuwjin.pogo.parser.RuleParser").add(result("new")).expression(
               seq(ref("Identifier").add(resultRef("return")), and(option(lit('='), lit('<'))))));
         add(rule("RuleAttributes", "org.fuwjin.pogo.parser.RuleParser").expression(
               seq(ref("EQUALS"), ref("Namespace").add(resultRef("namespace")), optional(ref("RuleInit").add(
                     resultRef("add"))), optional(ref("RuleMatch").add(resultRef("add"))), optional(ref("RuleResult")
                     .add(resultRef("add"))))));
         add(rule("RuleInit", "org.fuwjin.pogo.parser.RuleInitAttribute").add(result("new")).expression(
               seq(ref("HASH"), ref("Identifier").add(resultRef("return")))));
         add(rule("RuleMatch", "org.fuwjin.pogo.parser.RuleMatchAttribute").add(result("new")).expression(
               seq(ref("OUT"), ref("Identifier").add(resultRef("return")))));
         add(rule("RuleResult", "org.fuwjin.pogo.parser.RuleResultAttribute").add(result("new")).expression(
               seq(ref("COLON"), ref("Identifier").add(resultRef("return")))));
         add(rule("Namespace", "default")
               .expression(seq(ref("NamespaceIdent").add(matchRef("return")), ref("Spacing"))));
         add(rule("Function", "org.fuwjin.postage.CompositeFunction").add(result("new")).expression(
               ref("Identifier").add(resultRef("return"))));
         add(rule("Expression", "org.fuwjin.pogo.parser.OptionParser").add(init("new")).add(result("reduce"))
               .expression(
                     seq(ref("Sequence").add(resultRef("add")), star(seq(ref("SLASH"), ref("Sequence").add(
                           resultRef("add")))))));
         add(rule("Sequence", "org.fuwjin.pogo.parser.SequenceParser").add(init("new")).add(result("reduce"))
               .expression(plus(ref("Prefix").add(resultRef("add")))));
         add(rule("Prefix", "default").expression(
               option(ref("AND").add(resultRef("return")), ref("NOT").add(resultRef("return")), ref("Suffix").add(
                     resultRef("return")))));
         add(rule("Suffix", "default").expression(
               option(ref("QUESTION").add(resultRef("return")), ref("STAR").add(resultRef("return")), ref("PLUS").add(
                     resultRef("return")), ref("Primary").add(resultRef("return")))));
         add(rule("Primary", "default").expression(
               option(ref("Reference").add(resultRef("return")), seq(ref("OPEN"), ref("Expression").add(
                     resultRef("return")), ref("CLOSE")), ref("Literal").add(resultRef("return")), ref("CharClass")
                     .add(resultRef("return")), ref("DOT").add(resultRef("return")))));
         add(rule("Reference", "org.fuwjin.pogo.parser.RuleReferenceParser")
               .expression(
                     seq(ref("RefIdent").add(resultRef("return")), optional(ref("RefInit").add(resultRef("add"))),
                           optional(ref("RefMatch").add(resultRef("add"))), optional(ref("RefResult").add(
                                 resultRef("add"))))));
         add(rule("RefIdent", "org.fuwjin.pogo.parser.RuleReferenceParser").add(result("new")).expression(
               seq(ref("Identifier").add(resultRef("return")), not(option(lit('<'), lit('='))))));
         add(rule("RefInit", "org.fuwjin.pogo.parser.ReferenceInitAttribute").add(result("new")).expression(
               seq(ref("HASH"), ref("Identifier").add(resultRef("return")))));
         add(rule("RefMatch", "org.fuwjin.pogo.parser.ReferenceMatchAttribute").expression(
               seq(ref("OUT"), option(ref("RefMatchReturn").add(resultRef("return")), ref("RefMatchAttr").add(
                     resultRef("return"))))));
         add(rule("RefMatchReturn", "org.fuwjin.pogo.parser.ReferenceMatchAttribute").add(result("RETURN")).expression(
               seq(seq(lit('r'), lit('e'), lit('t'), lit('u'), lit('r'), lit('n')), ref("Spacing"))));
         add(rule("RefMatchAttr", "org.fuwjin.pogo.parser.ReferenceMatchAttribute").add(result("new")).expression(
               ref("Identifier").add(resultRef("return"))));
         add(rule("RefResult", "org.fuwjin.pogo.parser.ReferenceResultAttribute").expression(
               seq(ref("COLON"), option(ref("RefResultReturn").add(resultRef("return")), ref("RefResultAttr").add(
                     resultRef("return"))))));
         add(rule("RefResultReturn", "org.fuwjin.pogo.parser.ReferenceResultAttribute").add(result("RETURN"))
               .expression(seq(seq(lit('r'), lit('e'), lit('t'), lit('u'), lit('r'), lit('n')), ref("Spacing"))));
         add(rule("RefResultAttr", "org.fuwjin.pogo.parser.ReferenceResultAttribute").add(result("new")).expression(
               ref("Identifier").add(resultRef("return"))));
         add(rule("Literal", "org.fuwjin.pogo.parser.SequenceParser").add(init("new")).add(result("reduce"))
               .expression(
                     option(seq(lit('\''), star(seq(not(lit('\'')), ref("LitChar").add(resultRef("add")))), lit('\''),
                           ref("Spacing")), seq(lit('"'),
                           star(seq(not(lit('"')), ref("LitChar").add(resultRef("add")))), lit('"'), ref("Spacing")))));
         add(rule("CharClass", "org.fuwjin.pogo.parser.OptionParser").add(init("new")).add(result("reduce"))
               .expression(
                     seq(lit('['), star(seq(not(lit(']')), option(ref("Range").add(resultRef("add")), ref("LitChar")
                           .add(resultRef("add"))))), lit(']'), ref("Spacing"))));
         add(rule("Range", "org.fuwjin.pogo.parser.CharacterRangeParser").add(init("new")).expression(
               seq(ref("Char").add(resultRef("setStart")), lit('-'), ref("Char").add(resultRef("setEnd")))));
         add(rule("LitChar", "org.fuwjin.pogo.parser.CharacterLiteralParser").add(init("new")).expression(
               ref("Char").add(resultRef("set"))));
         add(rule("Char", "default").expression(
               option(seq(lit('\\'), ref("EscapeChar").add(resultRef("return"))), ref("PlainChar").add(
                     matchRef("return")))));
         add(rule("PlainChar", "default").expression(seq(not(lit('\\')), dot())));
         add(rule("EscapeChar", "default")
               .expression(
                     option(ref("Operator").add(matchRef("return")), ref("ControlChar").add(resultRef("return")), ref(
                           "OctalChar").add(resultRef("return")), seq(lit('x'), ref("UnicodeChar").add(
                           resultRef("return"))))));
         add(rule("Operator", "default").expression(
               option(lit('-'), lit('\''), lit('"'), lit('['), lit(']'), lit('\\'))));
         add(rule("Identifier", "default").expression(seq(ref("Ident").add(matchRef("return")), ref("Spacing"))));
         add(rule("NamespaceIdent", "default").expression(
               star(seq(not(option(lit('~'), lit('>'), lit(':'), lit('<'), lit(' '), lit('\n'), lit('\r'), lit('\t'))),
                     dot()))));
         add(rule("Ident", "default").expression(seq(ref("IdentStart"), star(ref("IdentCont")))));
         add(rule("IdentCont", "default").expression(option(ref("IdentStart"), range('0', '9'))));
         add(rule("IdentStart", "default").expression(option(range('a', 'z'), range('A', 'Z'), lit('_'))));
         add(rule("ControlChar", "org.fuwjin.pogo.parser.CharacterLiteralParser").add(match("slash")).expression(
               option(lit('n'), lit('r'), lit('t'))));
         add(rule("OctalChar", "org.fuwjin.pogo.parser.CharacterLiteralParser").add(match("octal")).expression(
               option(seq(range('0', '3'), range('0', '7'), range('0', '7')), seq(range('0', '7'), optional(range('0',
                     '7'))))));
         add(rule("UnicodeChar", "org.fuwjin.pogo.parser.CharacterLiteralParser").add(match("unicode")).expression(
               plus(option(range('0', '9'), range('A', 'F'), range('a', 'f')))));
         add(rule("LEFTARROW", "default").expression(seq(seq(lit('<'), lit('-')), ref("Spacing"))));
         add(rule("EQUALS", "default").expression(seq(lit('='), ref("Spacing"))));
         add(rule("HASH", "default").expression(seq(lit('~'), ref("Spacing"))));
         add(rule("OUT", "default").expression(seq(lit('>'), ref("Spacing"))));
         add(rule("COLON", "default").expression(seq(lit(':'), ref("Spacing"))));
         add(rule("AND", "org.fuwjin.pogo.parser.PositiveLookaheadParser").add(init("new")).expression(
               seq(lit('&'), ref("Spacing"), ref("Suffix").add(resultRef("parser")))));
         add(rule("NOT", "org.fuwjin.pogo.parser.NegativeLookaheadParser").add(init("new")).expression(
               seq(lit('!'), ref("Spacing"), ref("Suffix").add(resultRef("parser")))));
         add(rule("QUESTION", "org.fuwjin.pogo.parser.OptionalParser").add(init("new")).expression(
               seq(ref("Primary").add(resultRef("parser")), lit('?'), ref("Spacing"))));
         add(rule("STAR", "org.fuwjin.pogo.parser.OptionalSeriesParser").add(init("new")).expression(
               seq(ref("Primary").add(resultRef("parser")), lit('*'), ref("Spacing"))));
         add(rule("PLUS", "org.fuwjin.pogo.parser.RequiredSeriesParser").add(init("new")).expression(
               seq(ref("Primary").add(resultRef("parser")), lit('+'), ref("Spacing"))));
         add(rule("OPEN", "default").expression(seq(lit('('), ref("Spacing"))));
         add(rule("CLOSE", "default").expression(seq(lit(')'), ref("Spacing"))));
         add(rule("SLASH", "default").expression(seq(lit('/'), ref("Spacing"))));
         add(rule("DOT", "org.fuwjin.pogo.parser.CharacterParser").add(init("new")).expression(
               seq(lit('.'), ref("Spacing"))));
         add(rule("Spacing", "default").expression(star(option(ref("Space"), ref("Comment")))));
         add(rule("Comment", "default").expression(
               seq(lit('#'), star(seq(not(ref("EndOfLine")), dot())), ref("EndOfLine"))));
         add(rule("Space", "default").expression(option(lit(' '), lit('\t'), ref("EndOfLine"))));
         add(rule("EndOfLine", "default").expression(option(seq(lit('\r'), lit('\n')), option(lit('\r'), lit('\n')))));
         add(rule("EndOfFile", "default").expression(not(dot())));
         resolve();
      } catch(final RuntimeException e) {
         e.printStackTrace();
      }
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
