package org.fuwjin.chessur;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.fuwjin.chessur.expression.AbortedException;
import org.fuwjin.chessur.expression.Expression;
import org.fuwjin.chessur.expression.ResolveException;
import org.fuwjin.chessur.stream.CodePointInStream;
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.ObjectOutStream;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.dinah.adapter.StandardAdapter;

public class ScriptState {
   private final SourceStream input;
   private final SinkStream output;
   private final Environment scope;

   /**
    * Executes the script with no input, output or initial environment. Returns
    * the result of the script or null if there is no return statement in the
    * script.
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState() {
      this(CodePointInStream.NONE, ObjectOutStream.NONE, Environment.NONE);
   }

   /**
    * Executes the script with a byte-based InputStream input, no output and no
    * initial environment. Returns the result of the script or null if there is
    * no return statement in the script.
    * @param input the byte-based input
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final InputStream input) {
      this(CodePointInStream.stream(input), ObjectOutStream.NONE, Environment.NONE);
   }

   /**
    * Executes the script with a byte-based InputStream input, no output and an
    * initial environment. Returns the result of the script or null if there is
    * no return statement in the script.
    * @param input the byte-based input
    * @param environment the initial environment
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final InputStream input, final Map<String, ? extends Object> environment) {
      this(CodePointInStream.stream(input), ObjectOutStream.NONE, new Environment(environment));
   }

   /**
    * Executes the script with a byte-based InputStream input, a PrintStream
    * output and no initial environment. Returns the result of the script or
    * null if there is no return statement in the script.
    * @param input the byte-based input
    * @param output the output
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final InputStream input, final PrintStream output) {
      this(CodePointInStream.stream(input), ObjectOutStream.stream(output), Environment.NONE);
   }

   /**
    * Executes the script with a byte-based InputStream input, a PrintStream
    * output and an initial environment. Returns the result of the script or
    * null if there is no return statement in the script.
    * @param input the byte-based input
    * @param output the output
    * @param environment the initial environment
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final InputStream input, final PrintStream output, final Map<String, ? extends Object> environment) {
      this(CodePointInStream.stream(input), ObjectOutStream.stream(output), new Environment(environment));
   }

   /**
    * Executes the script with a byte-based InputStream input, a Writer output
    * and no initial environment. Returns the result of the script or null if
    * there is no return statement in the script.
    * @param input the byte-based input
    * @param output the output
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final InputStream input, final Writer output) {
      this(CodePointInStream.stream(input), ObjectOutStream.stream(output), Environment.NONE);
   }

   /**
    * Executes the script with a byte-based InputStream input, a Writer output
    * and an initial environment. Returns the result of the script or null if
    * there is no return statement in the script.
    * @param input the byte-based input
    * @param output the output
    * @param environment the initial environment
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final InputStream input, final Writer output, final Map<String, ? extends Object> environment) {
      this(CodePointInStream.stream(input), ObjectOutStream.stream(output), new Environment(environment));
   }

   /**
    * Executes the script with no input, no output and an initial environment.
    * Returns the result of the script or null if there is no return statement
    * in the script.
    * @param environment the initial environment
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final Map<String, ? extends Object> environment) {
      this(CodePointInStream.NONE, ObjectOutStream.NONE, new Environment(environment));
   }

   /**
    * Executes the script with no input, a PrintStream output and no initial
    * environment. Returns the result of the script or null if there is no
    * return statement in the script.
    * @param output the output
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final PrintStream output) {
      this(CodePointInStream.NONE, ObjectOutStream.stream(output), Environment.NONE);
   }

   /**
    * Executes the script with no input, a PrintStream output and an initial
    * environment. Returns the result of the script or null if there is no
    * return statement in the script.
    * @param output the output
    * @param environment the initial environment
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final PrintStream output, final Map<String, ? extends Object> environment) {
      this(CodePointInStream.NONE, ObjectOutStream.stream(output), new Environment(environment));
   }

   /**
    * Executes the script with a code point-based Reader input, no output and no
    * initial environment. Returns the result of the script or null if there is
    * no return statement in the script.
    * @param input the code point-based input
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final Reader input) {
      this(CodePointInStream.stream(input), ObjectOutStream.NONE, Environment.NONE);
   }

   /**
    * Executes the script with a code point-based Reader input, no output and an
    * initial environment. Returns the result of the script or null if there is
    * no return statement in the script.
    * @param input the code point-based input
    * @param environment the initial environment
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final Reader input, final Map<String, ? extends Object> environment) {
      this(CodePointInStream.stream(input), ObjectOutStream.NONE, new Environment(environment));
   }

   /**
    * Executes the script with a code point-based Reader input, a PrintStream
    * output and no initial environment. Returns the result of the script or
    * null if there is no return statement in the script.
    * @param input the code point-based input
    * @param output the output
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final Reader input, final PrintStream output) {
      this(CodePointInStream.stream(input), ObjectOutStream.stream(output), Environment.NONE);
   }

   /**
    * Executes the script with a code point-based Reader input, a PrintStream
    * output and an initial environment. Returns the result of the script or
    * null if there is no return statement in the script.
    * @param input the code point-based input
    * @param output the output
    * @param environment the initial environment
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final Reader input, final PrintStream output, final Map<String, ? extends Object> environment) {
      this(CodePointInStream.stream(input), ObjectOutStream.stream(output), new Environment(environment));
   }

   /**
    * Executes the script with a code point-based Reader input, a Writer output
    * and no initial environment. Returns the result of the script or null if
    * there is no return statement in the script.
    * @param input the code point-based input
    * @param output the output
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final Reader input, final Writer output) {
      this(CodePointInStream.stream(input), ObjectOutStream.stream(output), Environment.NONE);
   }

   /**
    * Executes the script with a code point-based Reader input, a Writer output
    * and an initial environment. Returns the result of the script or null if
    * there is no return statement in the script.
    * @param input the code point-based input
    * @param output the output
    * @param environment the initial environment
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final Reader input, final Writer output, final Map<String, ? extends Object> environment) {
      this(CodePointInStream.stream(input), ObjectOutStream.stream(output), new Environment(environment));
   }

   public ScriptState(final SourceStream input, final SinkStream output, final Environment scope) {
      this.input = input;
      this.output = output;
      this.scope = scope;
   }

   /**
    * Executes the script with no input, a Writer output and no initial
    * environment. Returns the result of the script or null if there is no
    * return statement in the script.
    * @param output the output
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final Writer output) {
      this(CodePointInStream.NONE, ObjectOutStream.stream(output), Environment.NONE);
   }

   /**
    * Executes the script with no input, a Writer output and an initial
    * environment. Returns the result of the script or null if there is no
    * return statement in the script.
    * @param output the output
    * @param environment the initial environment
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   public ScriptState(final Writer output, final Map<String, ? extends Object> environment) {
      this(CodePointInStream.NONE, ObjectOutStream.stream(output), new Environment(environment));
   }

   public Object exec(final Expression expression) throws AbortedException, ResolveException {
      final Object value = expression.resolve(input, output, scope);
      if(StandardAdapter.isSet(value)) {
         return value;
      }
      return null;
   }

   public boolean isEof() {
      try {
         input.next(new Snapshot(input, output, scope));
         return false;
      } catch(final ResolveException e) {
         return true;
      }
   }

   @Override
   public String toString() {
      return new Snapshot(input, output, scope).toString();
   }
}
