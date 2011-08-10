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

import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import org.fuwjin.chessur.Script;
import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;
import org.fuwjin.grin.env.StandardEnv;

/**
 * Base utility class for all things transformative.
 */
public abstract class Executable implements Script {
   @Override
   public Script acceptFrom(final InputStream input) {
      return acceptFrom(StandardEnv.acceptFrom(input));
   }

   @Override
   public Script acceptFrom(final Reader input) {
      return acceptFrom(StandardEnv.acceptFrom(input));
   }

   @Override
   public Script acceptFrom(final Source input) {
      return Execution.acceptFrom(this, input);
   }

   @Override
   public Object exec() throws ExecutionException {
      return Execution.exec(this);
   }

   @Override
   public Script logTo(final Logger log) {
      return logTo(StandardEnv.logTo(log));
   }

   @Override
   public Script logTo(final PrintStream log) {
      return logTo(StandardEnv.logTo(log));
   }

   @Override
   public Script logTo(final Sink log) {
      return Execution.logTo(this, log);
   }

   @Override
   public Script logTo(final Writer log) {
      return logTo(StandardEnv.logTo(log));
   }

   @Override
   public abstract String name();

   @Override
   public Script publishTo(final PrintStream output) {
      return publishTo(StandardEnv.publishTo(output));
   }

   @Override
   public Script publishTo(final Sink output) {
      return Execution.publishTo(this, output);
   }

   @Override
   public Script publishTo(final Writer output) {
      return publishTo(StandardEnv.publishTo(output));
   }

   @Override
   public Script withState(final Map<String, ? extends Object> environment) {
      return withState(StandardEnv.withState(environment));
   }

   @Override
   public Script withState(final Scope environment) {
      return Execution.withState(this, environment);
   }

   protected abstract Expression expression();
}
