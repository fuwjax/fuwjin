package org.fuwjin.chessur.expression;

import org.fuwjin.chessur.Module;
import org.fuwjin.grin.env.Trace;

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

   /**
    * Returns the declaring module.
    * @return the declaring module
    */
   public Module module() {
      return module;
   }

   @Override
   public String name() {
      return module.name() + ":" + script.name();
   }

   @Override
   public Object resolve(final Trace trace)
         throws AbortedException, ResolveException {
      return expression().resolve(trace);
   }

   /**
    * Returns the proxied script.
    * @return the script
    */
   public Executable script() {
      return script;
   }

   @Override
   protected Expression expression() {
      return script.expression();
   }
}
