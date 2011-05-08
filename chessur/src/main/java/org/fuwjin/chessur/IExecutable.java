package org.fuwjin.chessur;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface IExecutable {
   Object exec() throws ExecutionException;

   Object exec(final InputStream input) throws ExecutionException;

   Object exec(final InputStream input, final Map<String, ? extends Object> scope) throws ExecutionException;

   Object exec(final InputStream input, final PrintStream output) throws ExecutionException;

   Object exec(final InputStream input, final PrintStream output, final Map<String, ? extends Object> scope)
         throws ExecutionException;

   Object exec(final InputStream input, final Writer output) throws ExecutionException;

   Object exec(final InputStream input, final Writer output, final Map<String, ? extends Object> scope)
         throws ExecutionException;

   Object exec(final Map<String, ? extends Object> scope) throws ExecutionException;

   Object exec(final PrintStream output) throws ExecutionException;

   Object exec(final PrintStream output, final Map<String, ? extends Object> scope) throws ExecutionException;

   Object exec(final Reader input) throws ExecutionException;

   Object exec(final Reader input, final Map<String, ? extends Object> scope) throws ExecutionException;

   Object exec(final Reader input, final PrintStream output) throws ExecutionException;

   Object exec(final Reader input, final PrintStream output, final Map<String, ? extends Object> scope)
         throws ExecutionException;

   Object exec(final Reader input, final Writer output) throws ExecutionException;

   Object exec(final Reader input, final Writer output, final Map<String, ? extends Object> scope)
         throws ExecutionException;

   Object exec(final Writer output) throws ExecutionException;

   Object exec(final Writer output, final Map<String, ? extends Object> scope) throws ExecutionException;

   String name();
}
