package org.fuwjin.chessur;

import java.util.HashMap;
import java.util.Map;
import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import org.fuwjin.chessur.expression.CatalogImpl;
import org.fuwjin.chessur.expression.CatalogProxy;
import org.fuwjin.grin.env.StandardTrace;

public class ChessurScript extends CompiledScript {
   private final ScriptEngine engine;
   private final Script catalog;
   private final Map<String, ChessurScript> modules;

   public ChessurScript(final Script catalog, final ScriptEngine engine) {
      this.catalog = catalog;
      this.engine = engine;
      modules = new HashMap<String, ChessurScript>();
      if(catalog instanceof CatalogImpl) {
         for(final CatalogProxy proxy: ((CatalogImpl)catalog).modules()) {
            modules.put(proxy.name(), new ChessurScript(proxy.catalog(), engine));
         }
      }
   }

   @Deprecated
   public Script catalog() {
      return catalog;
   }

   @Override
   public Object eval(final ScriptContext context) throws ScriptException {
      final Bindings bindings = new SimpleBindings();
      for(final int scope: context.getScopes()) {
         final Map<String, Object> engineBindings = engine.getBindings(scope);
         if(engineBindings != null) {
            bindings.putAll(engineBindings);
         }
         final Map<String, Object> scopeBindings = context.getBindings(scope);
         if(scopeBindings != null) {
            bindings.putAll(scopeBindings);
         }
      }
      Script script = catalog;
      if(bindings.containsKey("main")) {
         script = ((CatalogImpl)catalog).get((String)bindings.get("main"));
      }
      return script
            .eval(new StandardTrace(context.getReader(), context.getWriter(), bindings, context.getErrorWriter()));
   }

   @Override
   public ScriptEngine getEngine() {
      return engine;
   }

   @Deprecated
   public Iterable<Map.Entry<String, ChessurScript>> modules() {
      return modules.entrySet();
   }
}
