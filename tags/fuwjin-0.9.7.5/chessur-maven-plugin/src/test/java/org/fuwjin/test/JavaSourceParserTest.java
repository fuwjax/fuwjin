package org.fuwjin.test;

import org.fuwjin.chessur.CatalogManagerImpl;
import org.junit.BeforeClass;
import org.junit.Test;

public class JavaSourceParserTest {
   private static CatalogTestHarness harness;

   @BeforeClass
   public static void setup() throws Exception {
      harness = new CatalogTestHarness(new CatalogManagerImpl().loadCat("org/fuwjin/chessur/compiler/JavaGrammar.cat"));
   }

   @Test
   public void testExpression() throws Exception {
      harness.assertExec("Expression", "SomeClass.this.arr[23] = 'X'", "", null);
      harness.assertExec("Expression", "123.", "", null);
      harness.assertExec("Expression", "(123.)", "", null);
      harness.assertExec("Expression", "x = 14", "", null);
      harness.assertExec("Expression", "x = 14", "", null);
      harness.assertExec("Expression", "this.x = \"hello, world!\"", "", null);
      harness.assertExec("Expression", "super.x = 14", "", null);
   }

   @Test
   public void testFloatLiteral() throws Exception {
      // decimal float
      harness.assertExec("Literal", "123.", "", null);
      harness.assertExec("Literal", "123.456", "", null);
      harness.assertExec("Literal", "123f", "", null);
      harness.assertExec("Literal", "123e12f", "", null);
      harness.assertExec("Literal", "123.456F", "", null);
      harness.assertExec("Literal", "123.456E53D", "", null);
      harness.assertExec("Literal", "123.456E-53D", "", null);
      harness.assertExec("Literal", ".456", "", null);
      harness.assertExec("Literal", ".456e1", "", null);
      harness.assertExec("Literal", ".0E0", "", null);
      harness.assertExec("Literal", "2E2", "", null);
      harness.assertExec("Literal", "1d", "", null);
      // hex float
      harness.assertExec("Literal", "0x0.0p0", "", null);
      harness.assertExec("Literal", "0x1.2p3f", "", null);
      harness.assertExec("Literal", "0x.2p3", "", null);
      harness.assertExec("Literal", "0x2345.p3", "", null);
      harness.assertExec("Literal", "0x2345.p-3", "", null);
   }

   @Test
   public void testFormalParameterList() throws Exception {
      harness.assertExec("FormalParameterList", "final String name, final int id", "", null);
      harness.assertExec("FormalParameterList", "final String name", "", null);
      harness.assertExec("FormalParameterList", "final int id", "", null);
   }

   @Test
   public void testIntLiteral() throws Exception {
      // decimal int
      harness.assertExec("Literal", "0", "", null);
      harness.assertExec("Literal", "7", "", null);
      harness.assertExec("Literal", "17777", "", null);
      harness.assertExec("Literal", "17777l", "", null);
      harness.assertExec("Literal", "54634L", "", null);
      // hex int
      harness.assertExec("Literal", "0x0", "", null);
      harness.assertExec("Literal", "0X0000", "", null);
      harness.assertExec("Literal", "0XFFFFL", "", null);
      // octal int
      harness.assertExec("Literal", "00000", "", null);
      harness.assertExec("Literal", "007", "", null);
      harness.assertExec("Literal", "0123l", "", null);
   }

   @Test
   public void testType() throws Exception {
      harness.assertExec("Type", "int", "", null);
      harness.assertExec("Type", "String", "", null);
   }
}
