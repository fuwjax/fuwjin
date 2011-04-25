package org.fuwjin.test;

import static org.fuwjin.util.StreamUtils.reader;
import static org.fuwjin.util.StringUtils.readAll;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.InStream;
import org.fuwjin.chessur.OutStream;
import org.fuwjin.dinah.ReflectiveFunctionProvider;
import org.fuwjin.util.Parameterized;
import org.fuwjin.util.Parameterized.Parameters;
import org.fuwjin.util.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class CanonicalDemo {
   private static Catalog catParser;
   private static Catalog catFormatter;
   private static Catalog catSerializer;

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
      catParser = Catalog.loadCat(readAll(reader("grin.parse.cat", "UTF-8")));
      catFormatter = Catalog.loadCat(readAll(reader("grin.format.cat", "UTF-8")));
      catSerializer = Catalog.loadCat(readAll(reader("grin.serial.cat", "UTF-8")));
   }

   private final File path;

   public CanonicalDemo(final String name, final File path) {
      this.path = path;
   }

   @Test
   public void testFormatting() throws Exception {
      final OutStream formatterOutput = OutStream.stream();
      catFormatter.transform(InStream.stream(newReader(".cat")), formatterOutput,
            Collections.<String, Object> emptyMap());
      assertThat(formatterOutput.toString(), is(StringUtils.readAll(newReader(".cat.canonical"))));
   }

   @Test
   public void testHardSerialization() throws Exception {
      final Catalog cat = Catalog.loadCat(StringUtils.readAll(newReader(".cat")));
      final OutStream serialOutput = OutStream.stream();
      catSerializer.transform(InStream.NONE, serialOutput, Collections.singletonMap("cat", cat));
      assertThat(serialOutput.toString(), is(StringUtils.readAll(newReader(".cat.canonical"))));
   }

   @Test
   public void testSerialization() throws Exception {
      final Catalog cat = (Catalog)catParser.transform(InStream.stream(newReader(".cat")),
            Collections.singletonMap("postage", new ReflectiveFunctionProvider()));
      final OutStream serialOutput = OutStream.stream();
      catSerializer.transform(InStream.NONE, serialOutput, Collections.singletonMap("cat", cat));
      assertThat(serialOutput.toString(), is(StringUtils.readAll(newReader(".cat.canonical"))));
   }

   private Reader newReader(final String suffix) throws FileNotFoundException {
      return new FileReader(new File(path, path.getName() + suffix));
   }
}
