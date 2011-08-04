package org.fuwjin.test;

import java.io.File;
import org.fuwjin.ChessurExecuteMojo;
import org.junit.Test;

/**
 * Tests the compiler behavior.
 */
public class ChessurExecuteMojoTest {
   /**
    * Executes the compiler.
    * @throws Exception if the test fails
    */
   @Test
   public void testMojo() throws Exception {
      final ChessurExecuteMojo mojo = new ChessurExecuteMojo("org/fuwjin/chessur/compiler/GrinCompiler.cat", new File(
            "src/test/grin"), new File("target/generated-test-catalogs"));
      mojo.execute();
   }
}
