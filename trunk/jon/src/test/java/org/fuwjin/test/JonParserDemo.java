package org.fuwjin.test;

import static org.fuwjin.util.StreamUtils.readAll;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.fuwjin.chessur.CatalogManager;
import org.fuwjin.jon.Registry;
import org.fuwjin.util.Parameterized;
import org.fuwjin.util.Parameterized.Parameters;
import org.fuwjin.util.UserFiles;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Demos script formatting.
 */
@RunWith(Parameterized.class)
public class JonParserDemo {
   private static CatalogManager manager;

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
   }

   private static Matcher<Object> matcher(final File file) throws Exception {
      if(file.exists()) {
         return (Matcher<Object>)manager.loadCat(file).exec();
      }
      return CoreMatchers.nullValue();
   }

   private final File path;

   /**
    * Creates a new instance.
    * @param name the name of the test
    * @param path the test directory
    */
   public JonParserDemo(final String name, final File path) {
      this.path = path;
   }

   /**
    * Tests that the serializer works for the test case.
    * @throws Exception if it fails
    */
   @Test
   public void testKnownParsing() throws Exception {
      final Object object = new Registry().load(newReader("demo.full.jon"));
      assertThat(object, matcher(file("matcher.cat")));
   }

   @Test
   public void testTypedParsing() throws Exception {
      try {
         final String[] types = readAll(newReader("type.txt")).split("\n");
         for(final String type: types) {
            if(type.trim().length() == 0) {
               continue;
            }
            final Type cls = (Type)manager.adapt(type.trim(), Type.class);
            final Object object = new Registry().load(newReader("demo.typed.jon"), cls);
            assertThat(object, matcher(file("matcher.cat")));
         }
      } catch(final FileNotFoundException e) {
         // do nothing?
      }
   }

   /**
    * Tests that the serializer works for the hard coded parser for the test
    * case.
    * @throws Exception if it fails
    */
   @Test
   public void testUnknownParsing() throws Exception {
      try {
         final Object object = new Registry().load(newReader("demo.simple.jon"));
         assertThat(object, matcher(file("matcher.cat")));
      } catch(final FileNotFoundException e) {
         // do nothing?
      }
   }

   private File file(final String file) {
      return new File(path, file);
   }

   private Reader newReader(final String file) throws FileNotFoundException {
      return new FileReader(file(file));
   }
}
