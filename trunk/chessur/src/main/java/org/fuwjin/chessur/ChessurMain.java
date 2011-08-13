package org.fuwjin.chessur;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;
import org.fuwjin.util.StreamUtils;

/**
 * The command line runner for chessur.
 */
public class ChessurMain {
   /**
    * The Catalog/Script runner.
    * @param args the first arg must be a valid absolute/relative/classpath path
    *        to a Grin Catalog. the optional second arg may be a Script to
    *        execute within the Catalog. If the second arg is omitted, the
    *        Catalog will be executed directly.
    * @throws Exception if the catalog cannot load or the script fails
    */
   public static void main(final String... args) throws Exception {
      final ScriptEngineManager engines = new ScriptEngineManager();
      final ScriptEngine engine = engines.getEngineByName("chessur");
      final ScriptContext context = new SimpleScriptContext();
      context.setReader(new InputStreamReader(System.in));
      context.setWriter(new OutputStreamWriter(System.out));
      context.setErrorWriter(new OutputStreamWriter(System.err));
      final Bindings bindings = engine.createBindings();
      bindings.putAll((Map)System.getProperties());
      if(args.length > 1) {
         bindings.put("main", args[1]);
      }
      context.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
      final Object value = engine.eval(StreamUtils.reader(args[0], "UTF-8"), context);
      if(value != null) {
         if(value instanceof Integer) {
            System.exit((Integer)value);
         }
         System.err.println();
         System.err.println("returned " + value);
      }
   }
}
