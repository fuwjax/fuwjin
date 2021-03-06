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

import org.fuwjin.dinah.Adapter;
import org.fuwjin.grin.env.Trace;

/**
 * Publishes a value to an outstream.
 */
public class PublishStatement implements Expression {
   private final Expression value;

   /**
    * Creates a new instance.
    * @param value the value to publish
    */
   public PublishStatement(final Expression value) {
      this.value = value;
   }

   @Override
   public Object resolve(final Trace trace) throws AbortedException, ResolveException {
      final Object result = value.resolve(trace);
      trace.publish(result);
      return Adapter.UNSET;
   }

   @Override
   public String toString() {
      return "publish " + value;
   }

   /**
    * Returns the published value.
    * @return the value
    */
   public Expression value() {
      return value;
   }
}
