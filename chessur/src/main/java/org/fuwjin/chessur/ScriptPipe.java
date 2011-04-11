package org.fuwjin.chessur;

public class ScriptPipe extends Transformer {
   private final Transformer sink;
   private final Transformer source;

   public ScriptPipe(final Transformer source, final Transformer sink) {
      this.source = source;
      this.sink = sink;
   }

   @Override
   public State transform(final State state) {
      final OutStream out = OutStream.stream();
      final State output = state.redirectOutput(out);
      if(!output.isSuccess()) {
         return state.failure(output, "Could not redirect output");
      }
      final State pipe = source.transform(output);
      if(!pipe.isSuccess()) {
         return state.failure(pipe, "Could not transform source");
      }
      final State input = pipe.restoreIo(state).redirectInput(InStream.streamOf(out.toString()));
      final State result = sink.transform(input);
      if(!result.isSuccess()) {
         return state.failure(result, "Could not transform sink");
      }
      return result.restoreIo(state);
   }
}
