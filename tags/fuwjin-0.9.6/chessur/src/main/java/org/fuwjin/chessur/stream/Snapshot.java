package org.fuwjin.chessur.stream;

import org.fuwjin.chessur.expression.AbortedException;
import org.fuwjin.chessur.expression.Expression;
import org.fuwjin.chessur.expression.ResolveException;

/**
 * Captures the current state of the input/output/environment.
 */
public class Snapshot {
   private final Position out;
   private final Position in;
   private final SourceStream input;
   private final SinkStream output;
   private final Environment scope;

   /**
    * Creates a new instance.
    * @param input the input
    * @param output the output
    * @param scope the environment
    */
   public Snapshot(final SourceStream input, final SinkStream output, final Environment scope) {
      this.input = input;
      this.output = output;
      this.scope = scope;
      in = input.current();
      out = output.current();
   }

   /**
    * Resolves the given statement against the captured state, possibly updating
    * the state following a successful parse.
    * @param statement the state to resolve against
    * @param update true if the detached streams should reattach.
    * @return the result of the statement resolution
    * @throws AbortedException if the script is intentionally aborted
    * @throws ResolveException if the script fails
    */
   public Object resolve(final Expression statement, final boolean update) throws AbortedException, ResolveException {
      final SourceStream inBuffer = input.detach();
      final SinkStream outBuffer = output.detach();
      final Object value = statement.resolve(inBuffer, outBuffer, scope);
      if(update) {
         input.attach(inBuffer);
         output.attach(outBuffer);
      }
      return value;
   }

   @Override
   public String toString() {
      return in + " -> " + out;
   }
}
