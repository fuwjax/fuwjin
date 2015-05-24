package org.fuwjin.test;

import static org.fuwjin.pogo.CodePointStreamFactory.streamBytes;
import static org.fuwjin.pogo.CodePointStreamFactory.streamOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoException;
import org.fuwjin.postage.type.Optional;
import org.junit.Test;

public class PogoGrammarTest {
   public static final String ZZZ = "zzz";

   public static String capitalize(final String match) {
      return match.toUpperCase();
   }

   @Test
   public void childRule() throws Exception {
      parse("G<-R R<-.*", "abcd", Optional.UNSET);
   }

   @Test
   public void dotStar() throws Exception {
      parse("G<-.*", "abcd", Optional.UNSET);
   }

   @Test
   public void dotStarCapitalizeResult() throws Exception {
      parse("G=org.fuwjin.test.PogoGrammarTest:{this=capitalize(match)}<-.*", "abcd", "ABCD");
   }

   @Test
   public void dotStarResult() throws Exception {
      parse("G=:{this=match}<-.*", "abcd", "abcd");
   }

   @Test
   public void extendedAttribute() throws Exception {
      parse("G<-R:{this=match} R<-.*", "abcd", "abcd");
   }

   @Test
   public void extendedAttributeFunction() throws Exception {
      parse("G<-R:{this=org.fuwjin.test.PogoGrammarTest.capitalize(match)} R<-.*", "abcd", "ABCD");
   }

   @Test
   public void extendedAttributeFunctionWithNamespace() throws Exception {
      parse("G=org.fuwjin.test.PogoGrammarTest<-R:{this=capitalize(match)} R<-.*", "abcd", "ABCD");
   }

   @Test
   public void extendedInitAttribute() throws Exception {
      parse("G<-R:{this=result} R=~{this=org.fuwjin.test.PogoGrammarTest.ZZZ()}<-.*", "abcd", "zzz");
   }

   @Test
   public void extendedResultAttribute() throws Exception {
      parse("G<-R:{this=result} R=:{this=match}<-.*", "abcd", "abcd");
   }

   @Test
   public void extendedResultAttributeFunction() throws Exception {
      parse("G<-R:{this=org.fuwjin.test.PogoGrammarTest.capitalize(result)} R=:{this=match}<-.*", "abcd", "ABCD");
   }

   @Test
   public void justDot() throws Exception {
      parse("G<-.", "a", Optional.UNSET);
   }

   @Test
   public void matchReturn() throws Exception {
      parse("G<-R>return R<-.*", "abcd", "abcd");
   }

   private void parse(final String grammar, final String input, final Object output) throws PogoException,
         FileNotFoundException {
      final Grammar enhancedPogo = Grammar.readGrammar(streamBytes("pogo.extended.parse.pogo"));
      final Grammar g = (Grammar)enhancedPogo.parse(streamOf(grammar));
      assertThat(g.parse(streamOf(input)), is(output));
   }

   @Test
   public void simpleDotStarCapitalizeResult() throws Exception {
      parse("G=org.fuwjin.test.PogoGrammarTest>capitalize<-.*", "abcd", "ABCD");
   }

   @Test
   public void simpleResultAttribute() throws Exception {
      parse("G=org.fuwjin.test.PogoGrammarTest<-R>capitalize R<-.*", "abcd", "ABCD");
   }
}
