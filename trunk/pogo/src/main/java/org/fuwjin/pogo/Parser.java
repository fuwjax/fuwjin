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

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.state.ParseState;
import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.pogo.state.SerialState;
import org.fuwjin.postage.type.Optional;

/**
 * The main parsing interface.
 */
public abstract class Parser {
   @Override
   public boolean equals(final Object obj) {
      try {
         final Parser o = (Parser)obj;
         return eq(getClass(), o.getClass()) && eq(expression(), o.expression());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   protected abstract ParsingExpression expression();

   @Override
   public int hashCode() {
      return hash(getClass(), expression());
   }

   /**
    * Parses the input stream.
    * @param input the input stream
    * @return the parsed object
    * @throws PogoException if the parse fails
    */
   public Object parse(final CodePointStream input) throws PogoException {
      return parse(input, Optional.UNSET);
   }

   /**
    * Parses the input into the object.
    * @param input the input stream
    * @param object the object to fill
    * @return the filled object
    * @throws PogoException if the parse fails
    */
   public Object parse(final CodePointStream input, final Object object) throws PogoException {
      return parse(new ParseState(input), object);
   }

   /**
    * Parses the context.
    * @param context the context
    * @throws PogoException if the parse fails
    */
   public void parse(final PogoState context) throws PogoException {
      if(!expression().parse(context)) {
         throw context.exception();
      }
   }

   /**
    * Parses from the position into the supplied object.
    * @param position the start position
    * @param object the object to fill
    * @return the memoized result
    * @throws PogoException if the parse fails
    */
   public Object parse(final PogoState position, final Object object) throws PogoException {
      position.setValue(object);
      if(!expression().parse(position)) {
         throw position.exception();
      }
      return position.getValue();
   }

   /**
    * Serializes the input into the string.
    * @param input the input to serialize
    * @param appender the destination for the serialization
    * @throws PogoException if the serialization fails
    */
   public void serial(final Object input, final Appendable appender) throws PogoException {
      final PogoState pos = new SerialState(appender);
      pos.setValue(input);
      if(!expression().parse(pos)) {
         throw pos.exception();
      }
   }

   /**
    * Serializes the object to a string.
    * @param object the object to serialize
    * @return the serialized string
    * @throws PogoException if the serialization fails
    */
   public String toString(final Object object) throws PogoException {
      final StringBuilder builder = new StringBuilder();
      serial(object, builder);
      return builder.toString();
   }
}
