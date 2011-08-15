package org.fuwjin.test;

import static org.fuwjin.util.StreamUtils.reader;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.ChessurScript;
import org.fuwjin.grin.env.StandardTrace;
import org.fuwjin.util.Parameterized;
import org.fuwjin.util.Parameterized.Parameters;
import org.fuwjin.util.RuntimeClassLoader;
import org.fuwjin.util.StreamUtils;
import org.fuwjin.util.UserFiles;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Demonstrates Grin Script execution.
 */
@RunWith(Parameterized.class)
public class ScriptExecutionDemo {
   private static final class TestData {
      private final File file;
      private CompiledScript cat;
      private Catalog modelCat;
      private Method interpreter;

      public TestData(final File file) {
         this.file = file;
      }

      public CompiledScript cat() throws Exception {
         if(cat == null) {
            cat = loadCatalog(file);
         }
         return cat;
      }

      public Method interpreter() throws Exception {
         if(interpreter == null) {
            interpreter = compile(file.getName(), cat());
         }
         return interpreter;
      }

      public Catalog modelCat() throws Exception {
         if(modelCat == null) {
            modelCat = parseModel(file);
         }
         return modelCat;
      }
   }

   private static ScriptEngine manager;
   private static CompiledScript catCodeGenerator;
   private static CompiledScript catParser;
   private static RuntimeClassLoader loader;

   /**
    * The parameters for the test.
    * @return the parameters
    */
   @Parameters
   public static Collection<Object[]> parameters() {
      final File catPath = new File("src/test/demo");
      final List<Object[]> list = new ArrayList<Object[]>();
      for(final File file: catPath.listFiles(new UserFiles())) {
         for(final File caseFolder: file.listFiles(new UserFiles())) {
            if(caseFolder.isDirectory()) {
               list.add(new Object[]{file.getName() + " case " + caseFolder.getName(), file, caseFolder,
                     new TestData(file)});
            }
         }
      }
      return list;
   }

   /**
    * Initializes the manager and standard catalogs.
    * @throws Exception if it fails
    */
   @BeforeClass
   public static void setUp() throws Exception {
      final ScriptEngineManager engines = new ScriptEngineManager();
      manager = engines.getEngineByName("chessur");
      catCodeGenerator = ((Compilable)manager).compile(reader("org/fuwjin/chessur/generated/GrinCodeGenerator.cat",
            "UTF-8"));
      catParser = ((Compilable)manager).compile(reader("org/fuwjin/chessur/generated/GrinParser.cat", "UTF-8"));
      loader = new RuntimeClassLoader();
   }

   static Method compile(final String simpleClassName, final CompiledScript cat) throws Exception {
      final Map<String, String> sources = new HashMap<String, String>();
      final String className = addSource(sources, simpleClassName, cat, simpleClassName);
      loader.compile(sources);
      final Class<?> parserClass = loader.loadClass(className);
      return parserClass.getMethod("interpret", CharSequence.class, Appendable.class, Appendable.class, Map.class);
   }

   static CompiledScript loadCatalog(final File file) throws ScriptException, IOException {
      return ((Compilable)manager).compile(reader(new File(file, file.getName() + ".cat").getPath(), "UTF-8"));
   }

   static Catalog parseModel(final File file) throws Exception {
      final ScriptContext context = new SimpleScriptContext();
      context.setReader(new FileReader(new File(file, file.getName() + ".cat")));
      context.setAttribute("name", file.getName(), ScriptContext.ENGINE_SCOPE);
      return (Catalog)catParser.eval(context);
   }

   private static String addSource(final Map<String, String> sources, final String simpleName,
         final CompiledScript cat, final String mainName) throws Exception {
      for(final Map.Entry<String, ChessurScript> module: ((ChessurScript)cat).modules()) {
         addSource(sources, module.getKey(), module.getValue(), mainName);
      }
      final String className = "org.fuwjin.internal.generated." + simpleName;
      final Writer code = new StringWriter();
      final Bindings environment = manager.createBindings();
      environment.put("cat", ((ChessurScript)cat).catalog());
      environment.put("package", "org.fuwjin.internal.generated");
      environment.put("className", mainName);
      environment.put("moduleName", simpleName);
      final ScriptContext context = new SimpleScriptContext();
      context.setWriter(code);
      context.setBindings(environment, ScriptContext.ENGINE_SCOPE);
      if(mainName.equals(simpleName)) {
         catCodeGenerator.eval(context);
      } else {
         environment.put("main", "Module");
         ((ChessurScript)catCodeGenerator).eval(context);
      }
      sources.put(className, code.toString());
      return className;
   }

   private static Bindings environment() {
      final Bindings env = manager.createBindings();
      env.put("var", "test");
      return env;
   }

   private static String firstLine(final String string) {
      final int index = string.indexOf('\n');
      if(index == -1) {
         return string;
      }
      return string.substring(0, index);
   }

   private static Matcher<Object> matcher(final File file) throws Exception {
      if(file.exists()) {
         return (Matcher<Object>)manager.eval(new FileReader(file));
      }
      return CoreMatchers.nullValue();
   }

   private final File caseFolder;
   private final TestData data;

   /**
    * Creates a new instance.
    * @param name the name of the test
    * @param file the test directory
    * @param caseFolder the test case
    * @param data the test catalogs
    */
   public ScriptExecutionDemo(final String name, final File file, final File caseFolder, final TestData data) {
      this.caseFolder = caseFolder;
      this.data = data;
   }

   /**
    * Tests that the code generator works for the test case.
    * @throws Exception if it fails
    */
   @Test
   public void testCodeGeneration() throws Exception {
      try {
         final StringBuilder codeOutput = new StringBuilder();
         final Object codeResult = data.interpreter().invoke(null, StreamUtils.readAll(newReader("input.txt")),
               codeOutput, new StringBuilder(), environment());
         assertEquals(codeOutput.toString(), StreamUtils.readAll(newReader("output.txt")));
         assertThat(codeResult, is(matcher(file("matcher.cat"))));
      } catch(final InvocationTargetException e) {
         assertEquals(e.getCause().getMessage(), firstLine(StreamUtils.readAll(newReader("error.txt"))));
      }
   }

   /**
    * Tests that the cat parser works for the test case.
    * @throws Exception if it fails
    */
   @Test
   public void testModelParsing() throws Exception {
      final Writer output = new StringWriter();
      try {
         final Object result = data.modelCat().eval(
               new StandardTrace(newReader("input.txt"), output, new StringWriter(), environment()));
         assertEquals(output.toString(), StreamUtils.readAll(newReader("output.txt")));
         assertThat(result, is(matcher(file("matcher.cat"))));
      } catch(final ScriptException e) {
         assertEquals(e.getCause().getMessage(), StreamUtils.readAll(newReader("error.txt")));
      }
   }

   /**
    * Tests that the compiled parser works for the test case.
    * @throws Exception if it fails
    */
   @Test
   public void testParsing() throws Exception {
      final Writer output = new StringWriter();
      try {
         final ScriptContext context = new SimpleScriptContext();
         context.setWriter(output);
         context.setBindings(environment(), ScriptContext.ENGINE_SCOPE);
         context.setReader(newReader("input.txt"));
         final Object result = data.cat().eval(context);
         assertEquals(output.toString(), StreamUtils.readAll(newReader("output.txt")));
         assertThat(result, is(matcher(file("matcher.cat"))));
      } catch(final ScriptException e) {
         assertEquals(e.getCause().getMessage(), StreamUtils.readAll(newReader("error.txt")));
      }
   }

   private File file(final String suffix) {
      return new File(caseFolder, suffix);
   }

   private Reader newReader(final String suffix) throws FileNotFoundException, UnsupportedEncodingException {
      final File f = file(suffix);
      if(f.exists()) {
         return new InputStreamReader(new FileInputStream(f), "UTF-8");
      }
      return new StringReader("");
   }
}
