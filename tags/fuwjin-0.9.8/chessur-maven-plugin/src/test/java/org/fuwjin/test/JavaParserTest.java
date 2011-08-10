package org.fuwjin.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.CatalogManagerImpl;
import org.fuwjin.util.Parameterized;
import org.fuwjin.util.Parameterized.Parameters;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the compiler behavior.
 */
@RunWith(Parameterized.class)
public class JavaParserTest {
   private static Catalog catalog;

   @Parameters
   public static Collection<Object[]> parameters() {
      final File catPath = new File("../");
      final List<Object[]> list = new ArrayList<Object[]>();
      add(list, catPath);
      return list;
   }

   @BeforeClass
   public static void setup() throws Exception {
      catalog = new CatalogManagerImpl().loadCat("org/fuwjin/chessur/compiler/JavaGrammar.cat");
   }

   private static void add(final List<Object[]> list, final File path) {
      for(final File file: path.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(final File dir, final String name) {
            return name.endsWith(".java") || !name.contains(".");
         }
      })) {
         if(file.isDirectory()) {
            add(list, file);
         } else {
            list.add(new Object[]{file.getPath(), file});
         }
      }
   }

   private final File source;

   public JavaParserTest(final String name, final File source) {
      this.source = source;
   }

   /**
    * Executes the compiler.
    * @throws Exception if the test fails
    */
   @Ignore
   @Test
   public void testMojo() throws Exception {
      final FileInputStream input = new FileInputStream(source);
      catalog.acceptFrom(input).publishTo(System.out).exec();
   }
}
