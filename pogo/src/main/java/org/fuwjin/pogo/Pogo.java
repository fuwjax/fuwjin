/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo;

import static org.fuwjin.io.BufferedInput.buffer;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.io.Reader;
import java.text.ParseException;

import org.fuwjin.io.ParseContext;
import org.fuwjin.io.PogoContext;
import org.fuwjin.io.PogoException;
import org.fuwjin.io.PogoRootContext;
import org.fuwjin.io.SerialContext;
import org.fuwjin.pogo.parser.Rule;

/**
 * The main parsing interface.
 */
public class Pogo {
   private Rule rule;

   /**
    * Creates a new instance.
    */
   protected Pogo() {
      // for subclasses
   }

   /**
    * Creates a new instance.
    * @param rule the rule to use as a start.
    */
   public Pogo(final Rule rule) {
      this.rule = rule;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final Pogo o = (Pogo)obj;
         return eq(getClass(), o.getClass()) && eq(rule, o.rule);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), rule);
   }

   /**
    * Parses the input into the object.
    * @param input the input stream
    * @param object the object to fill
    * @return the filled object
    * @throws ParseException if the parse fails
    */
   public Object parse(final CharSequence input, final Object object) throws PogoException {
      final PogoRootContext context = new ParseContext(input);
      final PogoContext child = context.newChild(rule.name());
      child.set(object, null);
      rule.parse(child);
      context.assertSuccess();
      return child.get();
   }

   /**
    * Parses the context.
    * @param context the context
    * @throws ParseException if the parse fails
    */
   public void parse(final PogoRootContext context) throws PogoException {
      rule.parse(context.newChild(rule.name()));
      context.assertSuccess();
   }

   public PogoContext parse(final PogoRootContext context, final Object obj) throws PogoException {
      final PogoContext child = context.newChild(rule.name());
      child.set(obj, null);
      rule.parse(child);
      context.assertSuccess();
      return child;
   }

   /**
    * Parses the input into an object.
    * @param reader the input reader
    * @return the parsed object
    * @throws ParseException if the parse fails
    */
   public Object parse(final Reader reader) throws PogoException {
      return parse(reader, null);
   }

   /**
    * Parses the input into an object.
    * @param reader the input reader
    * @param object the object to fill
    * @return the filled object
    * @throws ParseException if the parse fails
    */
   public Object parse(final Reader reader, final Object object) throws PogoException {
      return parse(buffer(reader), object);
   }

   /**
    * Parses the input into the object.
    * @param input the input stream
    * @return the parsed object
    * @throws ParseException if the parse fails
    */
   public Object parse(final String input) throws PogoException {
      return parse(input, null);
   }

   /**
    * Serializes the input into the string.
    * @param input the input to serialize
    * @return the serialized string
    */
   public String serial(final Object input) {
      final PogoRootContext context = new SerialContext();
      final PogoContext child = context.newChild(rule.name());
      child.set(input, null);
      rule.parse(child);
      return child.match();
   }

   /**
    * Sets the rule underlying this parser.
    * @param rule the parser
    */
   protected void setRule(final Rule rule) {
      this.rule = rule;
   }
}
