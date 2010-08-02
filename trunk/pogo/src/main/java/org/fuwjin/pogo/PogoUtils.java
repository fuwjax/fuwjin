/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo;

import static java.lang.Thread.currentThread;
import static org.fuwjin.pogo.PogoGrammar.pogoParseGrammar;
import static org.fuwjin.pogo.PredefinedGrammar.PogoSerial;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.fuwjin.io.PogoException;
import org.fuwjin.pogo.parser.CharacterLiteralParser;
import org.fuwjin.pogo.parser.CharacterParser;
import org.fuwjin.pogo.parser.CharacterRangeParser;
import org.fuwjin.pogo.parser.NegativeLookaheadParser;
import org.fuwjin.pogo.parser.OptionParser;
import org.fuwjin.pogo.parser.OptionalParser;
import org.fuwjin.pogo.parser.OptionalSeriesParser;
import org.fuwjin.pogo.parser.PositiveLookaheadParser;
import org.fuwjin.pogo.parser.RequiredSeriesParser;
import org.fuwjin.pogo.parser.Rule;
import org.fuwjin.pogo.parser.RuleReferenceParser;
import org.fuwjin.pogo.parser.SequenceParser;
import org.fuwjin.pogo.reflect.AllType;
import org.fuwjin.pogo.reflect.AppendTask;
import org.fuwjin.pogo.reflect.ClassType;
import org.fuwjin.pogo.reflect.ConstructorTask;
import org.fuwjin.pogo.reflect.ContextFinalizerTask;
import org.fuwjin.pogo.reflect.ContextInitializerTask;
import org.fuwjin.pogo.reflect.DefaultResultTask;
import org.fuwjin.pogo.reflect.FactoryTask;
import org.fuwjin.pogo.reflect.FinalizerTask;
import org.fuwjin.pogo.reflect.InitializerTask;
import org.fuwjin.pogo.reflect.InstanceOfTask;
import org.fuwjin.pogo.reflect.NextTask;
import org.fuwjin.pogo.reflect.NullTask;
import org.fuwjin.pogo.reflect.NullType;
import org.fuwjin.pogo.reflect.ObjectType;
import org.fuwjin.pogo.reflect.PassThruTask;
import org.fuwjin.pogo.reflect.ReferenceTask;
import org.fuwjin.pogo.reflect.ReflectionType;
import org.fuwjin.pogo.reflect.ResultTask;
import org.fuwjin.pogo.reflect.ReturnTask;
import org.fuwjin.pogo.reflect.StaticInitializerTask;

/**
 * The DSL for generating hardcoded Pogo parsers.
 */
public class PogoUtils { // NO_UCD
   /**
    * Creates an instance of initializer.
    * @return the initializer
    */
   public static InstanceOfTask _instanceof() {
      return new InstanceOfTask();
   }

   /**
    * Creates a default constructor initializer.
    * @return the initializer
    */
   public static ConstructorTask _new() {
      return new ConstructorTask();
   }

   /**
    * Creates a null type.
    * @return the type
    */
   public static NullType _null() {
      return new NullType();
   }

   /**
    * Creates a return finalizer.
    * @return the finalizer
    */
   public static ReturnTask _return() {
      return new ReturnTask();
   }

   /**
    * Creates a pass through initializer.
    * @return the initializer
    */
   public static PassThruTask _this() {
      return new PassThruTask();
   }

   /**
    * Creates an any type.
    * @return the type
    */
   public static AllType _true() {
      return new AllType();
   }

   /**
    * Creates a positive lookahead parser.
    * @param parser the inner parser
    * @return the new parser
    */
   public static Parser and(final Parser parser) {
      return new PositiveLookaheadParser(parser);
   }

   /**
    * Creates a dispatched finalizer.
    * @param name the name of the dispatched message
    * @return the finalizer
    */
   public static AppendTask append(final String name) {
      return new AppendTask(name);
   }

   /**
    * Creates a dispatched initializer.
    * @param name the name of the dispatched message
    * @return the initializer
    */
   public static FactoryTask build(final String name) {
      return new FactoryTask(name);
   }

   /**
    * Creates a context-targeted initializer.
    * @param name the name of the context method
    * @return the initializer
    */
   public static ContextInitializerTask contextInit(final String name) {
      return new ContextInitializerTask(name);
   }

   /**
    * Creates a context-targeted finalizer.
    * @param name the name of the context method
    * @return the finalizer
    */
   public static ContextFinalizerTask contextResult(final String name) {
      return new ContextFinalizerTask(name);
   }

   /**
    * Creates an any character parser.
    * @return the new parser
    */
   public static Parser dot() {
      return new CharacterParser();
   }

   /**
    * Creates a no-op initializer or finalizer.
    * @return the task
    */
   public static NullTask ignore() {
      return new NullTask();
   }

   /**
    * Creates a default initializer.
    * @return the initializer
    */
   public static ReferenceTask init() {
      return new ReferenceTask();
   }

   /**
    * Creates a dispatched initializer.
    * @param name the dispatched message
    * @return the initializer
    */
   public static StaticInitializerTask init(final String name) {
      return new StaticInitializerTask(name);
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
    * Creates a next element initializer.
    * @return the initializer
    */
   public static NextTask next() {
      return new NextTask();
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
    * Opens a file from the context classloader.
    * @param file the file to open
    * @return the reader for the file
    */
   public static Reader open(final String file) {
      return new InputStreamReader(currentThread().getContextClassLoader().getResourceAsStream(file));
   }

   /**
    * Opens a file from the context classloader.
    * @param file the file to open
    * @param encoding the character encoding for the file
    * @return the reader for the file
    */
   public static Reader open(final String file, final String encoding) {
      try {
         return new InputStreamReader(currentThread().getContextClassLoader().getResourceAsStream(file), encoding);
      } catch(final UnsupportedEncodingException e) {
         throw new RuntimeException(e);
      }
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
    * Creates a new Pogo parser from the Pogo formatted input.
    * @param reader the input
    * @return the new parser
    * @throws ParseException if the parser cannot be generated
    */
   public static Grammar readGrammar(final Reader reader) throws PogoException {
      return (Grammar)pogoParseGrammar().parse(reader);
   }

   /**
    * Creates a new Pogo parser from the Pogo formatted input.
    * @param file the input
    * @return the new parser
    * @throws ParseException if the parser cannot be generated
    */
   public static Grammar readGrammar(final String file) throws PogoException {
      final Reader reader = open(file);
      return readGrammar(reader);
   }

   /**
    * Creates a reference parser.
    * @param name the name of the rule to redirect to
    * @param initializer the initalizer before the rule is parsed
    * @param finalizer the finalizer after the rule is parsed
    * @return the parser
    */
   public static Parser ref(final String name, final InitializerTask initializer, final FinalizerTask finalizer) {
      return new RuleReferenceParser(name, initializer, finalizer);
   }

   /**
    * Creates a parsed match finalizer.
    * @return the finalizer
    */
   public static DefaultResultTask result() {
      return new DefaultResultTask();
   }

   /**
    * Creates a dispatched finalizer.
    * @param name the dispatched message
    * @return the finalizer
    */
   public static ResultTask result(final String name) {
      return new ResultTask(name);
   }

   /**
    * Creates a new Rule.
    * @param name the name of the rule
    * @param type the type bound to the rule
    * @param initializer the initializer before the rule is parsed
    * @param finalizer the finalizer after the rule is parsed
    * @param parser the expression to parse
    * @return the parser
    */
   public static Rule rule(final String name, final ReflectionType type, final InitializerTask initializer,
         final FinalizerTask finalizer, final Parser parser) {
      return new Rule(name, type, initializer, finalizer, parser);
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

   /**
    * Creates a new untyped type.
    * @return the type
    */
   public static ObjectType type() {
      return new ObjectType();
   }

   /**
    * Creates a new typed type.
    * @param type the type
    * @return the type
    */
   public static ClassType type(final Class<?> type) {
      return new ClassType(type);
   }

   /**
    * Serializes the input grammar to a string.
    * @param grammar the grammar to serialize
    * @return the serialized grammar
    */
   public static String writeGrammar(final Grammar grammar) {
      return PogoSerial.get().serial(grammar);
   }
}
