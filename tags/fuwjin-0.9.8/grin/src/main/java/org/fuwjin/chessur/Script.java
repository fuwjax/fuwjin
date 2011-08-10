package org.fuwjin.chessur;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;

/**
 * The executable script interface. Script executions have four central pieces
 * to their state: an input source, an output sink, an environment map, and a
 * return value.
 * <p>
 * The input source may be either an InputStream or a Reader. If the source is
 * an InputStream, then it will read bytes from the input. If the source is a
 * Reader, then Unicode code points will be read from the input.
 * <p>
 * The output sink may be either a PrintStream or a Writer. If the sink is a
 * PrintStream, then it will use the {@link PrintStream#print(Object)} method to
 * append to the stream. If the sink is a Writer, then it will use
 * {@link Writer#write(String)} to write objects which have been turned to
 * strings through the {@link String#valueOf(Object)} method.
 * <p>
 * The environment map provides the initial set of variable references to the
 * script. Note that this environment map may not be altered by the script.
 * <p>
 * The return value is the Java-realized object returned from the script, or
 * null if there was no return statement in the script.
 */
public interface Script {
   /**
    * Executes the script with a byte-based InputStream input, no output and no
    * initial environment. Returns the result of the script or null if there is
    * no return statement in the script.
    * @param input the byte-based input
    * @return the script
    */
   Script acceptFrom(final InputStream input);

   /**
    * Executes the script with a code point-based Reader input, no output and no
    * initial environment. Returns the result of the script or null if there is
    * no return statement in the script.
    * @param input the code point-based input
    * @return the result of the script
    */
   Script acceptFrom(final Reader input);

   Script acceptFrom(final Source input);

   /**
    * Executes the script with no input, output or initial environment. Returns
    * the result of the script or null if there is no return statement in the
    * script.
    * @return the result of the script
    * @throws ExecutionException if the script fails to execute
    */
   Object exec() throws ExecutionException;

   Script logTo(final Logger log);

   /**
    * Executes the script with no input, a PrintStream output and no initial
    * environment. Returns the result of the script or null if there is no
    * return statement in the script.
    * @param log the output
    * @return the result of the script
    */
   Script logTo(final PrintStream log);

   Script logTo(final Sink log);

   /**
    * Executes the script with no input, a Writer output and no initial
    * environment. Returns the result of the script or null if there is no
    * return statement in the script.
    * @param log the output
    * @return the result of the script
    */
   Script logTo(final Writer log);

   /**
    * Returns a name for this script. Note that the same underlying script may
    * have different names depending on how it was acquired.
    * @return the name
    */
   String name();

   /**
    * Executes the script with no input, a PrintStream output and no initial
    * environment. Returns the result of the script or null if there is no
    * return statement in the script.
    * @param output the output
    * @return the result of the script
    */
   Script publishTo(final PrintStream output);

   Script publishTo(Sink output);

   /**
    * Executes the script with no input, a Writer output and no initial
    * environment. Returns the result of the script or null if there is no
    * return statement in the script.
    * @param output the output
    * @return the result of the script
    */
   Script publishTo(final Writer output);

   /**
    * Executes the script with no input, no output and an initial environment.
    * Returns the result of the script or null if there is no return statement
    * in the script.
    * @param environment the initial environment
    * @return the result of the script
    */
   Script withState(final Map<String, ? extends Object> environment);

   Script withState(Scope environment);
}
