package org.fuwjin.chessur;

import static javax.script.ScriptEngine.ENGINE;
import static javax.script.ScriptEngine.ENGINE_VERSION;
import static javax.script.ScriptEngine.LANGUAGE;
import static javax.script.ScriptEngine.LANGUAGE_VERSION;
import static org.fuwjin.util.PropertiesUtil.load;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class ChessurScriptEngineFactory implements ScriptEngineFactory {
   private static final String EXTENSION = "javax.script.extension";
   private static final String MIME_TYPE = "javax.script.mime_type";
   private static final String NAMES = "javax.script.names";
   private static final String VALUE_DELIM = ";";
   private final Properties props;
   {
      try {
         props = load("factory.attributes.properties");
      } catch(final IOException e) {
         throw new RuntimeException("Cannot load properties for Chessur Script Engine");
      }
   }

   @Override
   public String getEngineName() {
      return get(ENGINE);
   }

   @Override
   public String getEngineVersion() {
      return get(ENGINE_VERSION);
   }

   @Override
   public List<String> getExtensions() {
      return getAll(EXTENSION);
   }

   @Override
   public String getLanguageName() {
      return get(LANGUAGE);
   }

   @Override
   public String getLanguageVersion() {
      return get(LANGUAGE_VERSION);
   }

   @Override
   public String getMethodCallSyntax(final String obj, final String m, final String... args) {
      final StringBuilder builder = new StringBuilder();
      builder.append(m).append('(').append(obj);
      for(final String arg: args) {
         builder.append(',').append(arg);
      }
      return builder.append(')').toString();
   }

   @Override
   public List<String> getMimeTypes() {
      return getAll(MIME_TYPE);
   }

   @Override
   public List<String> getNames() {
      return getAll(NAMES);
   }

   @Override
   public String getOutputStatement(final String toDisplay) {
      return "publish '" + toDisplay.replace("\\", "\\\\").replace("\'", "\\\'") + '\'';
   }

   @Override
   public Object getParameter(final String key) {
      return props.get(key);
   }

   @Override
   public String getProgram(final String... statements) {
      final StringBuilder builder = new StringBuilder("<Root> {\n");
      for(final String statement: statements) {
         builder.append(statement).append('\n');
      }
      return builder.append('}').toString();
   }

   @Override
   public ScriptEngine getScriptEngine() {
      return new ChessurScriptEngine(this);
   }

   private String get(final String key) {
      return (String)getParameter(key);
   }

   private List<String> getAll(final String key) {
      return Arrays.asList(get(key).split(VALUE_DELIM));
   }
}
