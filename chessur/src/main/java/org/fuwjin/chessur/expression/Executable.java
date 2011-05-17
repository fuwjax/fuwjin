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
import org.fuwjin.chessur.Script;
import org.fuwjin.chessur.stream.CodePointInStream;
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.ObjectOutStream;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.dinah.adapter.StandardAdapter;

/**
 * Base utility class for all things transformative.
 */
public abstract class Executable implements Script {
   @Override
   public Object exec() throws ExecutionException {
      return exec(CodePointInStream.NONE, ObjectOutStream.NONE, Environment.NONE);
   }

   @Override
   public Object exec(final InputStream input) throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.NONE, Environment.NONE);
   }

   @Override
   public Object exec(final InputStream input, final Map<String, ? extends Object> scope) throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.NONE, new Environment(scope));
   }

   @Override
   public Object exec(final InputStream input, final PrintStream output) throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.stream(output), Environment.NONE);
   }

   @Override
   public Object exec(final InputStream input, final PrintStream output, final Map<String, ? extends Object> scope)
         throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.stream(output), new Environment(scope));
   }

   @Override
   public Object exec(final InputStream input, final Writer output) throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.stream(output), Environment.NONE);
   }

   @Override
   public Object exec(final InputStream input, final Writer output, final Map<String, ? extends Object> scope)
         throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.stream(output), new Environment(scope));
   }

   @Override
   public Object exec(final Map<String, ? extends Object> scope) throws ExecutionException {
      return exec(CodePointInStream.NONE, ObjectOutStream.NONE, new Environment(scope));
   }

   @Override
   public Object exec(final PrintStream output) throws ExecutionException {
      return exec(CodePointInStream.NONE, ObjectOutStream.stream(output), Environment.NONE);
   }

   @Override
   public Object exec(final PrintStream output, final Map<String, ? extends Object> scope) throws ExecutionException {
      return exec(CodePointInStream.NONE, ObjectOutStream.stream(output), new Environment(scope));
   }

   @Override
   public Object exec(final Reader input) throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.NONE, Environment.NONE);
   }

   @Override
   public Object exec(final Reader input, final Map<String, ? extends Object> scope) throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.NONE, new Environment(scope));
   }

   @Override
   public Object exec(final Reader input, final PrintStream output) throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.stream(output), Environment.NONE);
   }

   @Override
   public Object exec(final Reader input, final PrintStream output, final Map<String, ? extends Object> scope)
         throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.stream(output), new Environment(scope));
   }

   @Override
   public Object exec(final Reader input, final Writer output) throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.stream(output), Environment.NONE);
   }

   @Override
   public Object exec(final Reader input, final Writer output, final Map<String, ? extends Object> scope)
         throws ExecutionException {
      return exec(CodePointInStream.stream(input), ObjectOutStream.stream(output), new Environment(scope));
   }

   @Override
   public Object exec(final Writer output) throws ExecutionException {
      return exec(CodePointInStream.NONE, ObjectOutStream.stream(output), Environment.NONE);
   }

   @Override
   public Object exec(final Writer output, final Map<String, ? extends Object> scope) throws ExecutionException {
      return exec(CodePointInStream.NONE, ObjectOutStream.stream(output), new Environment(scope));
   }

   @Override
   public abstract String name();

   protected Object exec(final SourceStream input, final SinkStream output, final Environment scope)
         throws ExecutionException {
      try {
         final Object value = expression().resolve(input, output, scope);
         if(StandardAdapter.isSet(value)) {
            return value;
         }
         return null;
      } catch(final AbortedException e) {
         throw new ExecutionException(String.format("Execution of %s aborted", name()), e);
      } catch(final ResolveException e) {
         throw new ExecutionException(String.format("Execution of %s failed", name()), e);
      }
   }

   protected abstract Expression expression() throws AbortedException;
}
