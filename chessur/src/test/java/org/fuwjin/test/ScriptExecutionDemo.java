package org.fuwjin.test;

import static org.fuwjin.chessur.Catalog.loadCat;
import static org.fuwjin.util.StreamUtils.reader;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
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
import org.fuwjin.util.Adapter;
import org.fuwjin.util.Parameterized;
import org.fuwjin.util.Parameterized.Parameters;
import org.fuwjin.util.RuntimeClassLoader;
import org.fuwjin.util.StreamUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class ScriptExecutionDemo {
   private static final class TestData {
      private final File file;
      private Catalog cat;
      private Method interpreter;

      public TestData(final File file) {
         this.file = file;
      }

      public Catalog cat() throws Exception {
         if(cat == null) {
            final Reader reader = new FileReader(new File(file, file.getName() + ".cat"));
            cat = Catalog.loadCat(StreamUtils.readAll(reader));
         }
         return cat;
      }

      public Method interpreter() throws Exception {
         if(interpreter == null) {
            interpreter = compile(file.getName(), cat());
         }
         return interpreter;
      }
   }

   private static Catalog catCodeGenerator;
   private static RuntimeClassLoader loader;

   @Parameters
   public static Collection<Object[]> parameters() {
      final File catPath = new File("src/test/resources/cat");
      final List<Object[]> list = new ArrayList<Object[]>();
      for(final File file: catPath.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(final File dir, final String name) {
            return !name.startsWith(".");
         }
      })) {
         for(int index = 1; index < Integer.MAX_VALUE; index++) {
            final File in = new File(file, file.getName() + ".cat.input." + index);
            final File out = new File(file, file.getName() + ".cat.output." + index);
            final File err = new File(file, file.getName() + ".cat.error." + index);
            final File match = new File(file, file.getName() + ".cat.matcher." + index);
            if(!in.canRead() && !out.canRead() && !err.canRead() && !match.canRead()) {
               break;
            }
            list.add(new Object[]{file.getName() + " case " + index, file, index, new TestData(file)});
         }
      }
      return list;
   }

   @BeforeClass
   public static void setUp() throws Exception {
      catCodeGenerator = Catalog.loadCat(StreamUtils.readAll(reader("grin.code.cat", "UTF-8")));
      loader = new RuntimeClassLoader();
   }

   private static Method compile(final String simpleClassName, final Catalog cat) throws Exception {
      final Writer code = new StringWriter();
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("cat", cat);
      environment.put("package", "org.fuwjin.internal.generated");
      environment.put("className", simpleClassName);
      catCodeGenerator.exec(code, environment);
      final String className = "org.fuwjin.internal.generated." + simpleClassName + "Interpreter";
      loader.compile(className, code.toString());
      final Class<?> parserClass = loader.loadClass(className);
      return parserClass.getMethod("interpret", CharSequence.class, Appendable.class, Map.class);
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

   private static Matcher<Object> matcher(final Reader reader) throws Exception {
      if(reader instanceof StringReader) {
         return CoreMatchers.nullValue();
      }
      return (Matcher<Object>)loadCat(StreamUtils.readAll(reader)).exec();
   }

   private final File file;
   private final int index;
   private final TestData data;

   public ScriptExecutionDemo(final String name, final File file, final int index, final TestData data) {
      this.file = file;
      this.index = index;
      this.data = data;
   }

   @Test
   public void testCodeGeneration() throws Exception {
      try {
         final StringBuilder codeOutput = new StringBuilder();
         final Object codeResult = data.interpreter().invoke(null, StreamUtils.readAll(newReader(".cat.input")),
               codeOutput, environment());
         assertThat(codeOutput.toString(), is(StreamUtils.readAll(newReader(".cat.output"))));
         assertThat(codeResult, is(matcher(newReader(".cat.matcher"))));
      } catch(final InvocationTargetException e) {
         assertThat(e.getCause().getMessage(), is(firstLine(StreamUtils.readAll(newReader(".cat.error")))));
      }
   }

   @Test
   public void testParsing() throws Exception {
      final Writer output = new StringWriter();
      try {
         final Object result = data.cat().exec(newReader(".cat.input"), output, environment());
         assertThat(output.toString(), is(StreamUtils.readAll(newReader(".cat.output"))));
         if(Adapter.isSet(result)) {
            assertThat(result, is(matcher(newReader(".cat.matcher"))));
         }
      } catch(final ExecutionException e) {
         assertThat(e.getCause().getMessage(), is(StreamUtils.readAll(newReader(".cat.error"))));
      }
   }

   private Reader newReader(final String suffix) throws FileNotFoundException {
      final File f = new File(file, file.getName() + suffix + "." + index);
      if(f.exists()) {
         return new FileReader(f);
      }
      return new StringReader("");
   }
}
