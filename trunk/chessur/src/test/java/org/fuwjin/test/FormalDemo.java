package org.fuwjin.test;

import static org.fuwjin.util.StreamUtils.reader;
import static org.fuwjin.util.StringUtils.readAll;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.InStream;
import org.fuwjin.chessur.OutStream;
import org.fuwjin.dinah.ReflectiveFunctionProvider;
import org.fuwjin.util.RuntimeClassLoader;
import org.fuwjin.util.StringUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

public class FormalDemo {
   private static final class CatTest implements Iterable<TestCase> {
      private final File file;

      public CatTest(final File file) {
         this.file = file;
      }

      public String canonical() throws IOException {
         return readAll(new FileReader(new File(file, name() + ".cat.canonical")));
      }

      public Reader catFile() throws FileNotFoundException {
         return new FileReader(new File(file, name() + ".cat"));
      }

      @Override
      public Iterator<TestCase> iterator() {
         return new Iterator<TestCase>() {
            private int index = 1;
            private TestCase current;
            private TestCase next = new TestCase(file, index);

            @Override
            public boolean hasNext() {
               return next.exists();
            }

            @Override
            public TestCase next() {
               if(!hasNext()) {
                  throw new NoSuchElementException();
               }
               current = next;
               next = new TestCase(file, ++index);
               return current;
            }

            @Override
            public void remove() {
               throw new UnsupportedOperationException();
            }
         };
      }

      public String name() {
         return file.getName();
      }
   }

   private static final class TestCase {
      private final File in;
      private final File out;
      private final File err;
      private final File match;
      private final String name;

      public TestCase(final File file, final int index) {
         in = new File(file, file.getName() + ".cat.input." + index);
         out = new File(file, file.getName() + ".cat.output." + index);
         err = new File(file, file.getName() + ".cat.error." + index);
         match = new File(file, file.getName() + ".cat.matcher." + index);
         name = "case " + index + " of " + file;
      }

      public String error() throws FileNotFoundException, IOException {
         if(err.exists()) {
            return StringUtils.readAll(new FileReader(err));
         }
         return "";
      }

      public boolean exists() {
         return in.canRead() || out.canRead() || err.canRead() || match.canRead();
      }

      public String firstError() throws FileNotFoundException, IOException {
         final String err = error();
         final int index = err.indexOf('\n');
         if(index == -1) {
            return err;
         }
         return err.substring(0, index);
      }

      public Reader input() throws FileNotFoundException {
         if(in.exists()) {
            return new FileReader(in);
         }
         return new StringReader("");
      }

      public Matcher<Object> matcher() {
         if(match.exists()) {
            return CoreMatchers.notNullValue();
         }
         return CoreMatchers.nullValue();
      }

      public String output() throws FileNotFoundException, IOException {
         if(out.exists()) {
            return StringUtils.readAll(new FileReader(out));
         }
         return "";
      }

      @Override
      public String toString() {
         return name;
      }
   }

   private static Catalog catParser;
   private static Catalog catFormatter;
   private static Catalog catSerializer;
   private static Catalog catCodeGenerator;

   @BeforeClass
   public static void setUp() throws Exception {
      catParser = Catalog.loadCat(readAll(reader("grin.parse.cat", "UTF-8")));
      catFormatter = Catalog.loadCat(readAll(reader("grin.format.cat", "UTF-8")));
      catSerializer = Catalog.loadCat(readAll(reader("grin.serial.cat", "UTF-8")));
      catCodeGenerator = Catalog.loadCat(readAll(reader("grin.code.cat", "UTF-8")));
   }

   private static Iterable<File> catFiles() {
      final File catPath = new File("src/test/resources/cat");
      return Arrays.asList(catPath.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(final File dir, final String name) {
            return !name.startsWith(".");
         }
      }));
   }

   private static Map<String, ? extends Object> environment() {
      final Map<String, Object> env = new HashMap<String, Object>();
      env.put("postage", new ReflectiveFunctionProvider());
      env.put("var", "test");
      return env;
   }

   @Test
   public void testCodeGeneration() throws Exception {
      for(final File file: catFiles()) {
         try {
            final CatTest catTest = new CatTest(file);
            final Catalog cat = (Catalog)catParser.transform(InStream.stream(catTest.catFile()), environment());
            final Method interpret = compile(catTest.name(), cat);
            for(final TestCase test: catTest) {
               try {
                  final StringBuilder codeOutput = new StringBuilder();
                  final Object codeResult = interpret.invoke(null, readAll(test.input()), codeOutput, environment());
                  final Object result = cat.transform(InStream.stream(test.input()), OutStream.NONE, environment());
                  assertThat(codeResult, is(result));
                  assertThat(codeOutput.toString(), is(test.output()));
               } catch(final InvocationTargetException e) {
                  assertThat(test.toString(), e.getCause().getMessage(), is(test.firstError()));
               }
            }
         } catch(final Exception e) {
            throw new RuntimeException("error in file: " + file, e);
         }
      }
   }

   @Test
   public void testFormatting() throws Exception {
      for(final File file: catFiles()) {
         try {
            final CatTest catTest = new CatTest(file);
            final OutStream formatterOutput = OutStream.stream();
            catFormatter.transform(InStream.stream(catTest.catFile()), formatterOutput,
                  Collections.<String, Object> emptyMap());
            assertThat(formatterOutput.toString(), is(catTest.canonical()));
         } catch(final Exception e) {
            throw new RuntimeException("error in file: " + file, e);
         }
      }
   }

   @Test
   public void testParsing() throws Exception {
      for(final File file: catFiles()) {
         try {
            final CatTest catTest = new CatTest(file);
            final Catalog cat = (Catalog)catParser.transform(InStream.stream(catTest.catFile()), environment());
            for(final TestCase test: catTest) {
               final OutStream output = OutStream.stream();
               try {
                  final Object result = cat.transform(InStream.stream(test.input()), output, environment());
                  assertThat(test.toString(), output.toString(), is(test.output()));
                  assertThat(test.toString(), result, is(test.matcher()));
               } catch(final RuntimeException e) {
                  assertThat(test.toString(), e.getMessage(), is(test.error()));
               }
            }
         } catch(final Exception e) {
            throw new RuntimeException("error in file: " + file, e);
         }
      }
   }

   @Test
   public void testSerialization() throws Exception {
      for(final File file: catFiles()) {
         try {
            final CatTest catTest = new CatTest(file);
            final Catalog cat = (Catalog)catParser.transform(InStream.stream(catTest.catFile()), environment());
            final OutStream serialOutput = OutStream.stream();
            catSerializer.transform(InStream.NONE, serialOutput, Collections.singletonMap("cat", cat));
            assertThat(serialOutput.toString(), is(catTest.canonical()));
         } catch(final Exception e) {
            throw new RuntimeException("error in file: " + file, e);
         }
      }
   }

   private Method compile(final String simpleClassName, final Catalog cat) throws SecurityException,
         NoSuchMethodException, ClassNotFoundException {
      final OutStream code = OutStream.stream();
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("cat", cat);
      environment.put("package", "org.fuwjin.internal.generated");
      environment.put("className", simpleClassName);
      catCodeGenerator.transform(InStream.STDIN, code, environment);
      final RuntimeClassLoader loader = new RuntimeClassLoader();
      final String className = "org.fuwjin.internal.generated." + simpleClassName + "Interpreter";
      loader.compile(className, code.toString());
      final Class<?> parserClass = loader.loadClass(className);
      return parserClass.getMethod("interpret", CharSequence.class, Appendable.class, Map.class);
   }
}
