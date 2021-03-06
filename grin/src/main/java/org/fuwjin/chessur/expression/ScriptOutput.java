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

import org.fuwjin.grin.env.Trace;

/**
 * An expression representing redirected script output.
 */
public class ScriptOutput implements Expression {
   private final Expression spec;
   private final String name;

   /**
    * Creates a new instance.
    * @param spec the script
    * @param name the variable set to the output stream
    */
   public ScriptOutput(final Expression spec, final String name) {
      this.spec = spec;
      this.name = name;
   }

   /**
    * Returns the variable name.
    * @return the variable
    */
   public String name() {
      return name;
   }

   @Override
   public Object resolve(final Trace trace) throws AbortedException, ResolveException {
      final Trace output = trace.newOutput();
      final Object result = spec.resolve(output);
      trace.put(name, output.toString());
      return result;
   }

   /**
    * Returns the script.
    * @return the script
    */
   public Expression spec() {
      return spec;
   }
}
