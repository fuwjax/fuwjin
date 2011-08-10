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

import java.io.StringReader;
import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;
import org.fuwjin.grin.env.StandardEnv;
import org.fuwjin.grin.env.Trace;

/**
 * An expression representing redirected script input.
 */
public class ScriptInput implements Expression {
   private final Expression spec;
   private final Expression value;

   /**
    * Creates a new instance.
    * @param script the script
    * @param value the redirected input
    */
   public ScriptInput(final Expression script, final Expression value) {
      spec = script;
      this.value = value;
   }

   @Override
   public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
         throws AbortedException, ResolveException {
      final Object val = value.resolve(input, output, scope, trace);
      final Source in = StandardEnv.acceptFrom(new StringReader(String.valueOf(val)));
      return spec.resolve(in, output, scope, trace.newInput(in));
   }

   /**
    * Returns the script.
    * @return the script
    */
   public Expression spec() {
      return spec;
   }

   /**
    * Returns the input value.
    * @return the value
    */
   public Expression value() {
      return value;
   }
}
