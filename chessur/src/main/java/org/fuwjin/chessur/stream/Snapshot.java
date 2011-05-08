package org.fuwjin.chessur.stream;

import org.fuwjin.chessur.AbortedException;
import org.fuwjin.chessur.Expression;
import org.fuwjin.chessur.ResolveException;

public class Snapshot {
   private final Position out;
   private final Position in;
   private final SourceStream input;
   private final SinkStream output;
   private final Environment scope;

   public Snapshot(final SourceStream input, final SinkStream output, final Environment scope) {
      this.input = input;
      this.output = output;
      this.scope = scope;
      in = input.current();
      out = output.current();
   }

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
