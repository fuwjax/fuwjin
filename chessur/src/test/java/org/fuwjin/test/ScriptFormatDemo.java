package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.fuwjin.chessur.CatalogManager;
import org.fuwjin.chessur.ICatalog;
import org.fuwjin.dinah.ReflectiveFunctionProvider;
import org.fuwjin.util.Parameterized;
import org.fuwjin.util.Parameterized.Parameters;
import org.fuwjin.util.StreamUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class ScriptFormatDemo {
   private static CatalogManager manager;
   private static ICatalog catParser;
   private static ICatalog catFormatter;
   private static ICatalog catSerializer;

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
         list.add(new Object[]{file.getName(), file});
      }
      return list;
   }

   @BeforeClass
   public static void setUp() throws Exception {
      manager = new CatalogManager();
      catParser = manager.loadCat("grin.parse.cat");
      catFormatter = manager.loadCat("grin.format.cat");
      catSerializer = manager.loadCat("grin.serial.cat");
   }

   private final File path;

   public ScriptFormatDemo(final String name, final File path) {
      this.path = path;
   }

   @Test
   public void testFormatting() throws Exception {
      final Writer formatterOutput = new StringWriter();
      catFormatter.exec(newReader(".cat"), formatterOutput);
      assertThat(formatterOutput.toString(), is(StreamUtils.readAll(newReader(".cat.canonical"))));
   }

   @Ignore
   @Test
   public void testHardSerialization() throws Exception {
      final ICatalog cat = manager.loadCat(StreamUtils.readAll(newReader(".cat")));
      final Writer serialOutput = new StringWriter();
      catSerializer.exec(serialOutput, Collections.singletonMap("cat", cat));
      assertThat(serialOutput.toString(), is(StreamUtils.readAll(newReader(".cat.canonical"))));
   }

   @Ignore
   @Test
   public void testSerialization() throws Exception {
      final ICatalog cat = (ICatalog)catParser.exec(newReader(".cat"),
            Collections.singletonMap("postage", new ReflectiveFunctionProvider()));
      final Writer serialOutput = new StringWriter();
      catSerializer.exec(serialOutput, Collections.singletonMap("cat", cat));
      assertThat(serialOutput.toString(), is(StreamUtils.readAll(newReader(".cat.canonical"))));
   }

   private Reader newReader(final String suffix) throws FileNotFoundException {
      return new FileReader(new File(path, path.getName() + suffix));
   }
}
