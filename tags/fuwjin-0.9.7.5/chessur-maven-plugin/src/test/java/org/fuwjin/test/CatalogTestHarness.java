package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.io.StringReader;
import java.io.StringWriter;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.expression.Executable;

public class CatalogTestHarness {
   private final Catalog catalog;

   public CatalogTestHarness(final Catalog catalog) {
      this.catalog = catalog;
   }

   public void assertExec(final String script, final String input, final String output, final Object result)
         throws Exception {
      final StringWriter writer = new StringWriter();
      final StringReader reader = new StringReader(input);
      final Object ret = ((Executable)catalog.get(script)).exec(reader, writer);
      assertThat("Script did not return expected result", ret, is(result));
      assertEquals("Writer did not match expected output", writer.toString(), output);
      assertTrue("Reader did not reach EOF", reader.read() == -1);
   }
}
