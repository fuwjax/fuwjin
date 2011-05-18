package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.CatalogManager;
import org.fuwjin.chessur.expression.CatalogImpl;
import org.fuwjin.chessur.expression.CatalogProxy;
import org.fuwjin.chessur.generated.GrinParser.GrinParserException;
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
      private Catalog cat;
      private Catalog modelCat;
      private Method interpreter;

      public TestData(final File file) {
         this.file = file;
      }

      public Catalog cat() throws Exception {
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

   private static CatalogManager manager;
   private static Catalog catCodeGenerator;
   private static Catalog catParser;
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
      manager = new CatalogManager();
      catCodeGenerator = manager.loadCat("org/fuwjin/chessur/generated/GrinCodeGenerator.cat");
      catParser = manager.loadCat("org/fuwjin/chessur/generated/GrinParser.cat");
      loader = new RuntimeClassLoader();
   }

   static Method compile(final String simpleClassName, final Catalog cat) throws Exception {
      final Map<String, String> sources = new HashMap<String, String>();
      final String className = addSource(sources, simpleClassName, cat, simpleClassName);
      loader.compile(sources);
      final Class<?> parserClass = loader.loadClass(className);
      return parserClass.getMethod("interpret", CharSequence.class, Appendable.class, Map.class);
   }

   static Catalog loadCatalog(final File file) throws GrinParserException, IOException {
      return manager.loadCat(new File(file, file.getName() + ".cat"));
   }

   static Catalog parseModel(final File file) throws FileNotFoundException, ExecutionException {
      final Map<String, Object> env = new HashMap<String, Object>();
      env.put("postage", manager);
      env.put("name", file.getName());
      env.put("manager", manager);
      return (Catalog)catParser.exec(new FileReader(new File(file, file.getName() + ".cat")), env);
   }

   private static String addSource(final Map<String, String> sources, final String simpleName, final Catalog cat,
         final String mainName) throws ExecutionException {
      for(final CatalogProxy module: ((CatalogImpl)cat).modules()) {
         addSource(sources, module.name(), module.catalog(), mainName);
      }
      final String className = "org.fuwjin.internal.generated." + simpleName;
      final Writer code = new StringWriter();
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("cat", cat);
      environment.put("package", "org.fuwjin.internal.generated");
      environment.put("className", mainName);
      environment.put("moduleName", simpleName);
      if(mainName.equals(simpleName)) {
         catCodeGenerator.exec(code, environment);
      } else {
         catCodeGenerator.get("Module").exec(code, environment);
      }
      sources.put(className, code.toString());
      return className;
   }

   private static Map<String, ? extends Object> environment() {
      final Map<String, Object> env = new HashMap<String, Object>();
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
         return (Matcher<Object>)manager.loadCat(file).exec();
      }
      return CoreMatchers.nullValue();
   }

   private final File caseFolder;
   private final TestData data;

   /**
    * Creates a new instance.
    * @param name the name of the test
    * @param file the test directory
    * @param index the test case
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
               codeOutput, environment());
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
         final Object result = data.modelCat().exec(newReader("input.txt"), output, environment());
         assertEquals(output.toString(), StreamUtils.readAll(newReader("output.txt")));
         assertThat(result, is(matcher(file("matcher.cat"))));
      } catch(final ExecutionException e) {
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
         final Object result = data.cat().exec(newReader("input.txt"), output, environment());
         assertEquals(output.toString(), StreamUtils.readAll(newReader("output.txt")));
         assertThat(result, is(matcher(file("matcher.cat"))));
      } catch(final ExecutionException e) {
         assertEquals(e.getCause().getMessage(), StreamUtils.readAll(newReader("error.txt")));
      }
   }

   private File file(final String suffix) {
      return new File(caseFolder, suffix);
   }

   private Reader newReader(final String suffix) throws FileNotFoundException {
      final File f = file(suffix);
      if(f.exists()) {
         return new FileReader(f);
      }
      return new StringReader("");
   }
}
