package org.fuwjin.test;

import static org.fuwjin.pogo.CodePointStreamFactory.streamOf;
import static org.fuwjin.pogo.PogoGrammar.readGrammar;

import org.fuwjin.pogo.Grammar;
import org.junit.Test;

public class PogoGrammarTest {
   @Test
   public void justDot() throws Exception {
      final Grammar g = Grammar.readGrammar(streamOf("G<-."));
      g.parse(streamOf("a"));
   }
}
