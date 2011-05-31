package org.fuwjin.test;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.CatalogManager;
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
   private static CatalogManager manager;
   private static Catalog catParser;
   private static Catalog catFormatter;
   private static Catalog catSerializer;

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
      manager = new CatalogManager();
      catParser = manager.loadCat("org/fuwjin/chessur/generated/GrinParser.cat");
      catFormatter = manager.loadCat("org/fuwjin/chessur/generated/GrinFormatter.cat");
      catSerializer = manager.loadCat("org/fuwjin/chessur/generated/GrinSerializer.cat");
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
      catFormatter.exec(newReader(".cat"), formatterOutput);
      assertEquals(formatterOutput.toString(), StreamUtils.readAll(newReader(".formatted.cat")));
   }

   /**
    * Tests that the serializer works for the hard coded parser for the test
    * case.
    * @throws Exception if it fails
    */
   @Test
   public void testHardSerialization() throws Exception {
      final Catalog cat = manager.loadCat(file(".cat"));
      final Writer serialOutput = new StringWriter();
      catSerializer.exec(serialOutput, Collections.singletonMap("cat", cat));
      assertEquals(serialOutput.toString(), StreamUtils.readAll(newReader(".formatted.cat")));
   }

   /**
    * Tests that the serializer works for the test case.
    * @throws Exception if it fails
    */
   @Test
   public void testSerialization() throws Exception {
      final Map<String, Object> env = new HashMap<String, Object>();
      env.put("name", path.getName());
      env.put("manager", manager);
      final Catalog cat = (Catalog)catParser.exec(newReader(".cat"), env);
      final Writer serialOutput = new StringWriter();
      catSerializer.exec(serialOutput, Collections.singletonMap("cat", cat));
      assertEquals(serialOutput.toString(), StreamUtils.readAll(newReader(".formatted.cat")));
   }

   private File file(final String suffix) {
      return new File(path, path.getName() + suffix);
   }

   private Reader newReader(final String suffix) throws FileNotFoundException {
      return new FileReader(file(suffix));
   }
}
