package org.fuwjin.test.attr;

import static org.fuwjin.pogo.CodePointStreamFactory.streamOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjin.pogo.attr.Grammar;
import org.junit.Test;

/**
 * Tests the new attr package.
 */
public class AttrTest {
   /**
    * Tests a literal grammar.
    * @throws Exception if it fails
    */
   @Test
   public void literalTest() throws Exception {
      final Grammar grammar = new Grammar() {
         {
            add("root", result(get("rule"), assign("this", match())));
            add("rule", lit("literal"));
         }
      };
      final String value = (String)grammar.transform(streamOf("literal"));
      assertThat(value, is("literal"));
   }
}
