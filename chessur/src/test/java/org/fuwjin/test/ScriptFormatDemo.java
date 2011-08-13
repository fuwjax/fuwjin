package org.fuwjin.test;

import static org.fuwjin.util.StreamUtils.reader;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.ChessurScript;
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
public class ScriptFormatDemo {
   private static ScriptEngine engine;
   private static CompiledScript catParser;
   private static CompiledScript catFormatter;
   private static CompiledScript catSerializer;

   /**
    * The parameters for the test.
    * @return the parameters
    */
   @Parameters
   public static Collection<Object[]> parameters() {
      final File catPath = new File("src/test/demo");
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
      catFormatter = ((Compilable)engine).compile(reader("org/fuwjin/chessur/generated/GrinFormatter.cat", "UTF-8"));
      catSerializer = ((Compilable)engine).compile(reader("org/fuwjin/chessur/generated/GrinSerializer.cat", "UTF-8"));
   }

   private final File path;

   /**
    * Creates a new instance.
    * @param name the name of the test
    * @param path the test directory
    */
   public ScriptFormatDemo(final String name, final File path) {
      this.path = path;
   }

   /**
    * Tests that the formatter works for the test case.
    * @throws Exception if it fails
    */
   @Test
   public void testFormatting() throws Exception {
      final Writer formatterOutput = new StringWriter();
      final ScriptContext context = new SimpleScriptContext();
      context.setReader(newReader(".cat"));
      context.setWriter(formatterOutput);
      catFormatter.eval(context);
      assertEquals(formatterOutput.toString(), StreamUtils.readAll(newReader(".formatted.cat")));
   }

   /**
    * Tests that the serializer works for the hard coded parser for the test
    * case.
    * @throws Exception if it fails
    */
   @Test
   public void testHardSerialization() throws Exception {
      final CompiledScript cat = ((Compilable)engine).compile(reader(file(".cat").getPath(), "UTF-8"));
      final Writer serialOutput = new StringWriter();
      final ScriptContext context = new SimpleScriptContext();
      context.setWriter(serialOutput);
      context.setAttribute("cat", ((ChessurScript)cat).catalog(), ScriptContext.ENGINE_SCOPE);
      catSerializer.eval(context);
      assertEquals(serialOutput.toString(), StreamUtils.readAll(newReader(".formatted.cat")));
   }

   /**
    * Tests that the serializer works for the test case.
    * @throws Exception if it fails
    */
   @Test
   public void testSerialization() throws Exception {
      final Bindings env = engine.createBindings();
      env.put("name", path.getName());
      final ScriptContext context = new SimpleScriptContext();
      context.setReader(newReader(".cat"));
      context.setBindings(env, ScriptContext.ENGINE_SCOPE);
      final Catalog cat = (Catalog)catParser.eval(context);
      final Writer serialOutput = new StringWriter();
      final ScriptContext serialContext = new SimpleScriptContext();
      serialContext.setWriter(serialOutput);
      serialContext.setAttribute("cat", cat, ScriptContext.ENGINE_SCOPE);
      catSerializer.eval(serialContext);
      assertEquals(serialOutput.toString(), StreamUtils.readAll(newReader(".formatted.cat")));
   }

   private File file(final String suffix) {
      return new File(path, path.getName() + suffix);
   }

   private Reader newReader(final String suffix) throws FileNotFoundException {
      return new FileReader(file(suffix));
   }
}
