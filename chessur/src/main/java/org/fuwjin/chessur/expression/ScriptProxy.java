package org.fuwjin.chessur.expression;

import org.fuwjin.chessur.Module;
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.SourceStream;

/**
 * Represents a namespaced proxy from a loaded module.
 */
public class ScriptProxy extends Executable implements Expression {
   private final Module module;
   private final Executable script;

   /**
    * Creates a new instance.
    * @param module the source module
    * @param script the proxied script
    */
   public ScriptProxy(final Module module, final Executable script) {
      this.module = module;
      this.script = script;
   }

   public Module module() {
      return module;
   }

   @Override
   public String name() {
      return module.name() + ":" + script.name();
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      return expression().resolve(input, output, scope);
   }

   public Executable script() {
      return script;
   }

   @Override
   protected Expression expression() throws AbortedException {
      return script.expression();
   }
}
