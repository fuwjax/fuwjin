/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.pogo;

import static org.fuwjin.pogo.PogoUtils._new;
import static org.fuwjin.pogo.PogoUtils._return;
import static org.fuwjin.pogo.PogoUtils._this;
import static org.fuwjin.pogo.PogoUtils.append;
import static org.fuwjin.pogo.PogoUtils.dot;
import static org.fuwjin.pogo.PogoUtils.ignore;
import static org.fuwjin.pogo.PogoUtils.init;
import static org.fuwjin.pogo.PogoUtils.lit;
import static org.fuwjin.pogo.PogoUtils.not;
import static org.fuwjin.pogo.PogoUtils.option;
import static org.fuwjin.pogo.PogoUtils.optional;
import static org.fuwjin.pogo.PogoUtils.plus;
import static org.fuwjin.pogo.PogoUtils.range;
import static org.fuwjin.pogo.PogoUtils.ref;
import static org.fuwjin.pogo.PogoUtils.result;
import static org.fuwjin.pogo.PogoUtils.rule;
import static org.fuwjin.pogo.PogoUtils.seq;
import static org.fuwjin.pogo.PogoUtils.star;
import static org.fuwjin.pogo.PogoUtils.type;

/**
 * The Pogo grammar for parsing pogo grammars.
 */
@SuppressWarnings("nls")
public final class PogoGrammar extends Grammar {
   {
      add(rule("Grammar", type(org.fuwjin.pogo.Grammar.class), _new(), result("resolve"), seq(ref("Spacing", ignore(),
            ignore()), plus(ref("Definition", ignore(), append("add"))), ref("EndOfFile", ignore(), ignore()))));
      add(rule("Definition", type(org.fuwjin.pogo.parser.Rule.class), _new(), result(), seq(ref("Identifier", ignore(),
            append("name")), optional(ref("TypeInfo", _this(), ignore())), ref("LEFTARROW", ignore(), ignore()), ref(
            "Expression", ignore(), append("parser")))));
      add(rule("TypeInfo", type(org.fuwjin.pogo.parser.Rule.class), init(), result(), seq(ref("EQUALS", ignore(),
            ignore()), ref("TypeName", ignore(), append("type")), optional(seq(ref("HASH", ignore(), ignore()), ref(
            "Initializer", ignore(), append("initializer")))), optional(seq(ref("COLON", ignore(), ignore()), ref(
            "Finalizer", ignore(), append("finalizer")))))));
      add(rule("TypeName", type(), init(), result(), option(ref("TRUE", ignore(), _return()), ref("NULL", ignore(),
            _return()), ref("ClassIdentifier", ignore(), _return()))));
      add(rule("Initializer", type(), init(), result(), option(ref("NEW", ignore(), _return()), ref("INSTANCEOF",
            ignore(), _return()), ref("ContextInit", ignore(), _return()), ref("InitMethod", ignore(), _return()))));
      add(rule("ContextInit", type(org.fuwjin.pogo.reflect.ContextInitializerTask.class), _new(), result(), seq(seq(
            lit('c'), lit('o'), lit('n'), lit('t'), lit('e'), lit('x'), lit('t'), lit('.')), ref("Identifier",
            ignore(), append("name")))));
      add(rule("InitMethod", type(org.fuwjin.pogo.reflect.StaticInitializerTask.class), _new(), result(), ref(
            "Identifier", ignore(), append("name"))));
      add(rule("Finalizer", type(), init(), result(), option(ref("ContextResult", ignore(), _return()), ref(
            "ResultMethod", ignore(), _return()))));
      add(rule("ContextResult", type(org.fuwjin.pogo.reflect.ContextFinalizerTask.class), _new(), result(), seq(seq(
            lit('c'), lit('o'), lit('n'), lit('t'), lit('e'), lit('x'), lit('t'), lit('.')), ref("Identifier",
            ignore(), append("name")))));
      add(rule("ResultMethod", type(org.fuwjin.pogo.reflect.ResultTask.class), _new(), result(), ref("Identifier",
            ignore(), append("name"))));
      add(rule("Expression", type(org.fuwjin.pogo.parser.OptionParser.class), _new(), result("reduce"), seq(ref(
            "Sequence", ignore(), append("add")), star(seq(ref("SLASH", ignore(), ignore()), ref("Sequence", ignore(),
            append("add")))))));
      add(rule("Sequence", type(org.fuwjin.pogo.parser.SequenceParser.class), _new(), result("reduce"), plus(ref(
            "Prefix", ignore(), append("add")))));
      add(rule("Prefix", type(), init(), result(), option(ref("AND", ignore(), _return()), ref("NOT", ignore(),
            _return()), ref("Suffix", ignore(), _return()))));
      add(rule("Suffix", type(), init(), result(), option(ref("QUESTION", ignore(), _return()), ref("STAR", ignore(),
            _return()), ref("PLUS", ignore(), _return()), ref("Primary", ignore(), _return()))));
      add(rule("Primary", type(), init(), result(), option(ref("Reference", ignore(), _return()), seq(ref("OPEN",
            ignore(), ignore()), ref("Expression", ignore(), _return()), ref("CLOSE", ignore(), ignore())), ref(
            "Literal", ignore(), _return()), ref("CharClass", ignore(), _return()), ref("DOT", ignore(), _return()))));
      add(rule("Reference", type(org.fuwjin.pogo.parser.RuleReferenceParser.class), _new(), result(), seq(ref(
            "Identifier", ignore(), append("ruleName")), not(option(lit('<'), lit('='))), optional(seq(ref("HASH",
            ignore(), ignore()), ref("Constructor", ignore(), append("constructor")))), optional(seq(ref("COLON",
            ignore(), ignore()), ref("Converter", ignore(), append("converter")))))));
      add(rule("Constructor", type(), init(), result(), option(ref("THIS", ignore(), _return()), ref("NEXT", ignore(),
            _return()), ref("ConstMethod", ignore(), _return()))));
      add(rule("ConstMethod", type(org.fuwjin.pogo.reflect.FactoryTask.class), _new(), result(), ref("Identifier",
            ignore(), append("name"))));
      add(rule("Converter", type(), init(), result(), option(ref("RETURN", ignore(), _return()), ref("ConvMethod",
            ignore(), _return()))));
      add(rule("ConvMethod", type(org.fuwjin.pogo.reflect.AppendTask.class), _new(), result(), ref("Identifier",
            ignore(), append("name"))));
      add(rule("ClassIdentifier", type(org.fuwjin.pogo.reflect.ClassType.class), _new(), result(), seq(ref(
            "ClassIdent", ignore(), append("type")), ref("Spacing", ignore(), ignore()))));
      add(rule("Identifier", type(), init(), result(), seq(ref("Ident", ignore(), _return()), ref("Spacing", ignore(),
            ignore()))));
      add(rule("Literal", type(org.fuwjin.pogo.parser.SequenceParser.class), _new(), result("reduce"), option(seq(
            lit('\''), star(seq(not(lit('\'')), ref("LitChar", ignore(), append("add")))), lit('\''), ref("Spacing",
                  ignore(), ignore())), seq(lit('"'),
            star(seq(not(lit('"')), ref("LitChar", ignore(), append("add")))), lit('"'), ref("Spacing", ignore(),
                  ignore())))));
      add(rule("CharClass", type(org.fuwjin.pogo.parser.OptionParser.class), _new(), result("reduce"), seq(lit('['),
            star(seq(not(lit(']')), option(ref("Range", ignore(), append("add")), ref("LitChar", ignore(),
                  append("add"))))), lit(']'), ref("Spacing", ignore(), ignore()))));
      add(rule("Range", type(org.fuwjin.pogo.parser.CharacterRangeParser.class), _new(), result(), seq(ref("Char",
            ignore(), append("setStart")), lit('-'), ref("Char", ignore(), append("setEnd")))));
      add(rule("LitChar", type(org.fuwjin.pogo.parser.CharacterLiteralParser.class), _new(), result(), ref("Char",
            ignore(), append("set"))));
      add(rule("Char", type(), init(), result(), option(seq(lit('\\'), ref("EscapeChar", ignore(), _return())), seq(
            not(lit('\\')), dot()))));
      add(rule("EscapeChar", type(), init(), result(), option(
            option(lit('\''), lit('"'), lit('['), lit(']'), lit('\\')), ref("ControlChar", ignore(), _return()), ref(
                  "OctalChar", ignore(), _return()), seq(lit('x'), ref("UnicodeChar", ignore(), _return())))));
      add(rule("ClassIdent", type(java.lang.Class.class), init(), result("forName"), seq(ref("Ident", ignore(),
            ignore()), star(seq(option(lit('.'), lit('$')), ref("Ident", ignore(), ignore()))))));
      add(rule("Ident", type(), init(), result(), seq(ref("IdentStart", ignore(), ignore()), star(ref("IdentCont",
            ignore(), ignore())))));
      add(rule("IdentCont", type(), init(), result(), option(ref("IdentStart", ignore(), ignore()), range('0', '9'))));
      add(rule("IdentStart", type(), init(), result(), option(range('a', 'z'), range('A', 'Z'), lit('_'))));
      add(rule("ControlChar", type(org.fuwjin.pogo.parser.CharacterLiteralParser.class), init(), result("slash"),
            option(lit('n'), lit('r'), lit('t'))));
      add(rule("OctalChar", type(org.fuwjin.pogo.parser.CharacterLiteralParser.class), init(), result("octal"), option(
            seq(range('0', '3'), range('0', '7'), range('0', '7')), seq(range('0', '7'), optional(range('0', '7'))))));
      add(rule("UnicodeChar", type(org.fuwjin.pogo.parser.CharacterLiteralParser.class), init(), result("unicode"),
            plus(option(range('0', '9'), range('A', 'F'), range('a', 'f')))));
      add(rule("NULL", type(org.fuwjin.pogo.reflect.NullType.class), _new(), result(), seq(seq(lit('n'), lit('u'),
            lit('l'), lit('l')), ref("Spacing", ignore(), ignore()))));
      add(rule("TRUE", type(org.fuwjin.pogo.reflect.AllType.class), _new(), result(), seq(seq(lit('t'), lit('r'),
            lit('u'), lit('e')), ref("Spacing", ignore(), ignore()))));
      add(rule("NEW", type(org.fuwjin.pogo.reflect.ConstructorTask.class), _new(), result(), seq(seq(lit('n'),
            lit('e'), lit('w')), ref("Spacing", ignore(), ignore()))));
      add(rule("NEXT", type(org.fuwjin.pogo.reflect.NextTask.class), _new(), result(), seq(seq(lit('n'), lit('e'),
            lit('x'), lit('t')), ref("Spacing", ignore(), ignore()))));
      add(rule("THIS", type(org.fuwjin.pogo.reflect.PassThruTask.class), _new(), result(), seq(seq(lit('t'), lit('h'),
            lit('i'), lit('s')), ref("Spacing", ignore(), ignore()))));
      add(rule("RETURN", type(org.fuwjin.pogo.reflect.ReturnTask.class), _new(), result(), seq(seq(lit('r'), lit('e'),
            lit('t'), lit('u'), lit('r'), lit('n')), ref("Spacing", ignore(), ignore()))));
      add(rule("INSTANCEOF", type(org.fuwjin.pogo.reflect.InstanceOfTask.class), _new(), result(), seq(seq(lit('i'),
            lit('n'), lit('s'), lit('t'), lit('a'), lit('n'), lit('c'), lit('e'), lit('o'), lit('f')), ref("Spacing",
            ignore(), ignore()))));
      add(rule("LEFTARROW", type(), init(), result(), seq(seq(lit('<'), lit('-')), ref("Spacing", ignore(), ignore()))));
      add(rule("COLON", type(), init(), result(), seq(lit(':'), ref("Spacing", ignore(), ignore()))));
      add(rule("SLASH", type(), init(), result(), seq(lit('/'), ref("Spacing", ignore(), ignore()))));
      add(rule("AND", type(org.fuwjin.pogo.parser.PositiveLookaheadParser.class), _new(), result(), seq(lit('&'), ref(
            "Spacing", ignore(), ignore()), ref("Suffix", ignore(), append("parser")))));
      add(rule("NOT", type(org.fuwjin.pogo.parser.NegativeLookaheadParser.class), _new(), result(), seq(lit('!'), ref(
            "Spacing", ignore(), ignore()), ref("Suffix", ignore(), append("parser")))));
      add(rule("QUESTION", type(org.fuwjin.pogo.parser.OptionalParser.class), _new(), result(), seq(ref("Primary",
            ignore(), append("parser")), lit('?'), ref("Spacing", ignore(), ignore()))));
      add(rule("STAR", type(org.fuwjin.pogo.parser.OptionalSeriesParser.class), _new(), result(), seq(ref("Primary",
            ignore(), append("parser")), lit('*'), ref("Spacing", ignore(), ignore()))));
      add(rule("PLUS", type(org.fuwjin.pogo.parser.RequiredSeriesParser.class), _new(), result(), seq(ref("Primary",
            ignore(), append("parser")), lit('+'), ref("Spacing", ignore(), ignore()))));
      add(rule("HASH", type(), init(), result(), seq(lit('~'), ref("Spacing", ignore(), ignore()))));
      add(rule("EQUALS", type(), init(), result(), seq(lit('='), ref("Spacing", ignore(), ignore()))));
      add(rule("OPEN", type(), init(), result(), seq(lit('('), ref("Spacing", ignore(), ignore()))));
      add(rule("CLOSE", type(), init(), result(), seq(lit(')'), ref("Spacing", ignore(), ignore()))));
      add(rule("DOT", type(org.fuwjin.pogo.parser.CharacterParser.class), _new(), result(), seq(lit('.'), ref(
            "Spacing", ignore(), ignore()))));
      add(rule("Spacing", type(), init(), result(), star(option(ref("Space", ignore(), ignore()), ref("Comment",
            ignore(), ignore())))));
      add(rule("Comment", type(), init(), result(), seq(lit('#'), star(seq(not(ref("EndOfLine", ignore(), ignore())),
            dot())), ref("EndOfLine", ignore(), ignore()))));
      add(rule("Space", type(), init(), result(), option(lit(' '), lit('\t'), ref("EndOfLine", ignore(), ignore()))));
      add(rule("EndOfLine", type(), init(), result(), option(seq(lit('\r'), lit('\n')), option(lit('\r'), lit('\n')))));
      add(rule("EndOfFile", type(), init(), result(), not(dot())));
      resolve();
   }
   private static final Grammar grammar = new PogoGrammar();

   /**
    * Return the grammar for parsing pogo files.
    * @return the grammar
    */
   public static Grammar pogoParseGrammar() {
      return grammar;
   }
}
