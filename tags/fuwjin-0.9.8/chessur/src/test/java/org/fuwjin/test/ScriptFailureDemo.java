package org.fuwjin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.CatalogManagerImpl;
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
   private static CatalogManagerImpl manager;
   private static Catalog catParser;

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
      manager = new CatalogManagerImpl();
      catParser = manager.loadCat("org/fuwjin/chessur/generated/GrinParser.cat");
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
         manager.loadCat(file("test.cat"));
         fail("These scripts should never pass");
      } catch(final ExecutionException e) {
         //         assertEquals(e.getCause().getMessage(), firstLine(StreamUtils.readAll(newReader("error.txt"))));
      }
   }

   /**
    * Tests that the serializer works for the test case.
    * @throws Exception if it fails
    */
   @Test
   public void testSoftParse() throws Exception {
      final Map<String, Object> env = new HashMap<String, Object>();
      env.put("name", path.getName());
      env.put("manager", manager);
      try {
         catParser.acceptFrom(newReader("test.cat")).withState(env).exec();
      } catch(final ExecutionException e) {
         assertEquals(e.getCause().getMessage(), StreamUtils.readAll(newReader("error.txt")));
      }
   }

   private File file(final String suffix) {
      return new File(path, suffix);
   }

   private Reader newReader(final String suffix) throws FileNotFoundException, UnsupportedEncodingException {
      return new InputStreamReader(new FileInputStream(file(suffix)), "UTF-8");
   }
}
