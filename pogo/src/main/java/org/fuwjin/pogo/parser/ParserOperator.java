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
package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.util.Map;

import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Pogo;
import org.fuwjin.pogo.PredefinedGrammar;
import org.fuwjin.pogo.reflect.ReflectionType;

/**
 * The standard base class for parse operators.
 */
public abstract class ParserOperator implements Parser {
   private static final String PREFIX_CHAIN = "PrefixChain"; //$NON-NLS-1$
   /**
    * The target parser for this operaton.
    */
   protected Parser parser;
   private static Pogo serial;

   /**
    * Creates a new instance.
    */
   protected ParserOperator() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param parser the operated upon parser
    */
   protected ParserOperator(final Parser parser) {
      this.parser = parser;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ParserOperator o = (ParserOperator)obj;
         return eq(getClass(), o.getClass()) && eq(parser, o.parser);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), parser);
   }

   @Override
   public void resolve(final Map<String, Parser> grammar, final ReflectionType ruleType) {
      parser.resolve(grammar, ruleType);
   }

   @Override
   public String toString() {
      if(serial == null) {
         serial = PredefinedGrammar.PogoSerial.grammar().get(PREFIX_CHAIN);
      }
      return serial.serial(this);
   }
}
