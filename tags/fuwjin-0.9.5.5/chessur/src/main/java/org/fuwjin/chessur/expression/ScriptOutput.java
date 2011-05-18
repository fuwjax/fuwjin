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

import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.ObjectOutStream;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.SourceStream;

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

   public String name() {
      return name;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final SinkStream out = ObjectOutStream.stream();
      final Object result = spec.resolve(input, out, scope);
      scope.assign(name, out.toString());
      return result;
   }

   public Expression spec() {
      return spec;
   }
}
