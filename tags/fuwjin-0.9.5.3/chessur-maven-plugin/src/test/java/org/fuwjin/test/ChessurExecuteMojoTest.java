package org.fuwjin.test;

import java.io.File;
import org.fuwjin.ChessurExecuteMojo;
import org.junit.Test;

public class ChessurExecuteMojoTest {
   @Test
   public void testMojo() throws Exception {
      final ChessurExecuteMojo mojo = new ChessurExecuteMojo("grin.compiler.cat", new File("src/test/grin"), new File(
            "target/generated-test-catalogs"));
      mojo.execute();
   }
}
