package org.fuwjin.chessur;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.ExecutionException;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

public class ChessurScriptEngine extends AbstractScriptEngine implements Compilable {
   private final ScriptEngineFactory factory;
   private final CatalogManagerImpl manager;

   public ChessurScriptEngine(final ScriptEngineFactory factory) {
      this.factory = factory;
      manager = new CatalogManagerImpl();
      put("manager", manager);
   }

   @Override
   public CompiledScript compile(final Reader script) throws ScriptException {
      try {
         return new ChessurScript(manager.loadCat(script), this);
      } catch(final IOException e) {
         throw new ScriptException(e);
      } catch(final ExecutionException e) {
         throw new ScriptException((Exception)e.getCause());
      }
   }

   @Override
   public CompiledScript compile(final String script) throws ScriptException {
      return compile(new StringReader(script));
   }

   @Override
   public Bindings createBindings() {
      return new SimpleBindings();
   }

   @Override
   public Object eval(final Reader reader, final ScriptContext eContext) throws ScriptException {
      return compile(reader).eval(eContext);
   }

   @Override
   public Object eval(final String script, final ScriptContext eContext) throws ScriptException {
      return eval(new StringReader(script), eContext);
   }

   @Override
   public ScriptEngineFactory getFactory() {
      return factory;
   }

   public CatalogManager manager() {
      return manager;
   }
}
