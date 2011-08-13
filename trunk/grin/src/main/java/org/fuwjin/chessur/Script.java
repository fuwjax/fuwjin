package org.fuwjin.chessur;

import java.io.PrintStream;
import java.io.Writer;
import javax.script.ScriptException;
import org.fuwjin.grin.env.Trace;

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
   Object eval(Trace trace) throws ScriptException;

   /**
    * Returns a name for this script. Note that the same underlying script may
    * have different names depending on how it was acquired.
    * @return the name
    */
   String name();
}
