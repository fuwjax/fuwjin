package org.fuwjin.test;

import static org.fuwjin.pogo.PogoUtils._new;
import static org.fuwjin.pogo.PogoUtils.init;
import static org.fuwjin.pogo.PogoUtils.lit;
import static org.fuwjin.pogo.PogoUtils.result;
import static org.fuwjin.pogo.PogoUtils.rule;
import static org.fuwjin.pogo.PogoUtils.type;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringReader;
import java.text.ParseException;

import org.fuwjin.pogo.Grammar;
import org.junit.Test;

/**
 * Tests Rule parsing.
 */
public class RuleTest {
   private static final String A = "a"; //$NON-NLS-1$

   /**
    * The stock parse.
    * @throws ParseException if it fails
    */
   @Test
   public void testDefault() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", type(), init(), result(), lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is(A));
   }

   /**
    * Tests field finaliation.
    * @throws ParseException if it fails
    */
   @Test
   public void testInstanceFieldFinalizer() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result("builder"), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj, is(StringBuilder.class));
      assertThat(obj.toString(), is("default constructor;")); //$NON-NLS-1$
   }

   /**
    * Tests method finalization.
    * @throws ParseException if it fails
    */
   @Test
   public void testInstanceFinalizer() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result("toResult"), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("default constructor;toResult;")); //$NON-NLS-1$
   }

   /**
    * Tests default constructor initialization.
    * @throws ParseException if it fails
    */
   @Test
   public void testNew() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result(), lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("default constructor;")); //$NON-NLS-1$
   }

   /**
    * Tests the static method finalizer.
    * @throws ParseException if it fails
    */
   @Test
   public void testStaticConstantFinalizer() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule(
                  "Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), init(), result("newInstance"), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("static newInstance;")); //$NON-NLS-1$
   }

   /**
    * Tests the static field finalizer.
    * @throws ParseException if it fails
    */
   @Test
   public void testStaticFieldFinalizer() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), init(), result("NULL"), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("static NULL;")); //$NON-NLS-1$
   }

   /**
    * Tests static method finalizer.
    * @throws ParseException if it fails
    */
   @Test
   public void testStaticFinalizer() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), init(), result("valueOf"), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("static valueOf:a;")); //$NON-NLS-1$
   }

   /**
    * Tests static initialization.
    * @throws ParseException if it fails
    */
   @Test
   public void testStaticInitializer() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule(
                  "Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), init("newInstance"), result(), lit('a'))); //$NON-NLS-1$//$NON-NLS-2$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("static newInstance;")); //$NON-NLS-1$
   }
}
