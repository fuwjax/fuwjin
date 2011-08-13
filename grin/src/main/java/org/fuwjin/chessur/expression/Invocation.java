/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.chessur.expression;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.dinah.Function;
import org.fuwjin.grin.env.Trace;

/**
 * 
 */
public class Invocation implements Expression {
   private Function function;
   private final List<Expression> params = new ArrayList<Expression>();

   /**
    * Adds a new parameter value.
    * @param value the new value
    */
   public void addParam(final Expression value) {
      params.add(value);
   }

   /**
    * Returns the function.
    * @return the function
    */
   public Function function() {
      return function;
   }

   /**
    * Returns the function name.
    * @return the function name
    */
   public String name() {
      return function.signature().name();
   }

   /**
    * Returns the number of arguments.
    * @return the number of arguments
    */
   public int paramCount() {
      return params.size();
   }

   /**
    * Returns the parameter values.
    * @return the parameters
    */
   public Iterable<Expression> params() {
      return params;
   }

   @Override
   public Object resolve(final Trace trace)
         throws AbortedException, ResolveException {
      final Object[] args = new Object[params.size()];
      int index = 0;
      for(final Expression param: params) {
         try {
            final Object result = param.resolve(trace);
            args[index++] = result;
         } catch(final ResolveException e) {
            throw trace.fail(e, "Could not resolve %s argument %d", name(), index);
         }
      }
      try {
         return function.invoke(args);
      } catch(final InvocationTargetException e) {
         throw trace.fail(e.getCause(), "Failure in invocation target %s", name());
      } catch(final AdaptException e) {
         throw trace.fail(e, "Could not invoke %s", name());
      }
   }

   /**
    * Sets the function.
    * @param function the new function
    */
   public void setFunction(final Function function) {
      this.function = function;
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder(name()).append("(");
      boolean first = true;
      for(final Expression param: params) {
         if(first) {
            first = false;
         } else {
            builder.append(", ");
         }
         builder.append(param);
      }
      return builder.append(")").toString();
   }
}
