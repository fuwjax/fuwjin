/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.text.ParseException;

import org.fuwjin.io.SerialStreamPosition;
import org.fuwjin.io.StreamPosition;
import org.fuwjin.postage.StandardAdaptable;

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

   public Object parse(final CodePointStream input) throws PogoException {
      return parse(new StreamPosition(input), StandardAdaptable.UNSET);
   }

   /**
    * Parses the input into the object.
    * @param input the input stream
    * @param object the object to fill
    * @return the filled object
    * @throws ParseException if the parse fails
    */
   public Object parse(final CodePointStream input, final Object object) throws PogoException {
      return parse(new StreamPosition(input), object);
   }

   /**
    * Parses the context.
    * @param context the context
    * @throws ParseException if the parse fails
    */
   public void parse(final Position context) throws PogoException {
      rule.parse(context);
      context.assertSuccess();
   }

   public Object parse(final Position position, final Object object) throws PogoException {
      position.createMemo(rule.name(), object);
      final Position next = rule.parse(position);
      next.assertSuccess();
      return next.releaseMemo(null).getValue();
   }

   /**
    * Serializes the input into the string.
    * @param input the input to serialize
    * @return the serialized string
    */
   public void serial(final Object input, final Appendable appender) throws PogoException {
      final Position pos = new SerialStreamPosition(appender);
      pos.createMemo(rule.name(), input);
      final Position last = rule.parse(pos);
      last.assertSuccess();
      pos.releaseMemo(null);
   }

   /**
    * Sets the rule underlying this parser.
    * @param rule the parser
    */
   protected void setRule(final Rule rule) {
      this.rule = rule;
   }

   public String toString(final Object object) throws PogoException {
      final StringBuilder builder = new StringBuilder();
      serial(object, builder);
      return builder.toString();
   }
}
