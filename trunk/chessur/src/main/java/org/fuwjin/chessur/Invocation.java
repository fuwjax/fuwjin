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
package org.fuwjin.chessur;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.dinah.Function;
import org.fuwjin.util.Adapter.AdaptException;

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

   public String name() {
      return function.name();
   }

   public int paramCount() {
      return params.size();
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws ResolveException, AbortedException {
      final Snapshot snapshot = new Snapshot(input, output, scope);
      final Object[] args = new Object[params.size()];
      int index = 0;
      for(final Expression param: params) {
         try {
            final Object result = param.resolve(input, output, scope);
            args[index++] = result;
         } catch(final ResolveException e) {
            throw e.addStackTrace(snapshot, "Could not resolve %s argument %d", name(), index);
         }
      }
      try {
         return function.invoke(args);
      } catch(final InvocationTargetException e) {
         throw new ResolveException(e.getCause(), snapshot, "Failure in invocation target %s", name());
      } catch(final AdaptException e) {
         throw new ResolveException(e, snapshot, "Could not invoke %s", name());
      }
   }

   public void setFunction(final Function function) {
      this.function = function;
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder(function.name()).append("(");
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
