package org.fuwjin.test;

import static org.fuwjin.util.StreamUtils.reader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import org.fuwjin.util.Parameterized;
import org.fuwjin.util.Parameterized.Parameters;
import org.fuwjin.util.StreamUtils;
import org.fuwjin.util.UserFiles;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Demos script formatting.
 */
@RunWith(Parameterized.class)
public class ScriptFailureDemo {
   private static CompiledScript catParser;
   private static ScriptEngine engine;

   /**
    * The parameters for the test.
    * @return the parameters
    */
   @Parameters
   public static Collection<Object[]> parameters() {
      final File catPath = new File("src/test/fail");
      final List<Object[]> list = new ArrayList<Object[]>();
      for(final File file: catPath.listFiles(new UserFiles())) {
         list.add(new Object[]{file.getName(), file});
      }
      return list;
   }

   /**
    * Sets up the test.
    * @throws Exception if it fails
    */
   @BeforeClass
   public static void setUp() throws Exception {
      final ScriptEngineManager engines = new ScriptEngineManager();
      engine = engines.getEngineByName("chessur");
      catParser = ((Compilable)engine).compile(reader("org/fuwjin/chessur/generated/GrinParser.cat", "UTF-8"));
   }

   private static String firstLine(final String string) {
      final int index = string.indexOf('\n');
      if(index == -1) {
         return string;
      }
      return string.substring(0, index);
   }

   private final File path;

   /**
    * Creates a new instance.
    * @param name the name of the test
    * @param path the test directory
    */
   public ScriptFailureDemo(final String name, final File path) {
      this.path = path;
   }

   /**
    * Tests that the serializer works for the test case.
    * @throws Exception if it fails
    */
   @Test
   public void testParse() throws Exception {
      try {
         engine.eval(newReader("test.cat"));
         fail("These scripts should never pass");
      } catch(final ScriptException e) {
         assertEquals(e.getCause().getMessage(), firstLine(StreamUtils.readAll(newReader("error.txt"))));
      }
   }

   /**
    * Tests that the serializer works for the test case.
    * @throws Exception if it fails
    */
   @Test
   public void testSoftParse() throws Exception {
      final ScriptContext context = new SimpleScriptContext();
      context.setReader(newReader("test.cat"));
      context.setAttribute("name", path.getName(), ScriptContext.ENGINE_SCOPE);
      try {
         catParser.eval(context);
      } catch(final ScriptException e) {
         assertEquals(e.getCause().getMessage(), StreamUtils.readAll(newReader("error.txt")));
      }
   }

   private Reader newReader(final String suffix) throws FileNotFoundException, UnsupportedEncodingException {
      return reader(new File(path, suffix).getPath(), "UTF-8");
   }
}
