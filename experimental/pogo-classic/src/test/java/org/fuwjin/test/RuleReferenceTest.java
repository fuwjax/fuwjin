package org.fuwjin.test;

import static org.fuwjin.pogo.PogoUtils._instanceof;
import static org.fuwjin.pogo.PogoUtils._new;
import static org.fuwjin.pogo.PogoUtils._return;
import static org.fuwjin.pogo.PogoUtils._this;
import static org.fuwjin.pogo.PogoUtils.append;
import static org.fuwjin.pogo.PogoUtils.build;
import static org.fuwjin.pogo.PogoUtils.ignore;
import static org.fuwjin.pogo.PogoUtils.init;
import static org.fuwjin.pogo.PogoUtils.lit;
import static org.fuwjin.pogo.PogoUtils.ref;
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
 * Tests the rule reference.
 */
public class RuleReferenceTest {
   private static final String A = "a"; //$NON-NLS-1$

   /**
    * Tests append finalization.
    * @throws ParseException if it fails
    */
   @Test
   public void testAddMethod() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule(
                  "Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result(), ref("Rule", ignore(), append("addChild")))); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            add(rule("Rule", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result(), lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("default constructor;adding:default constructor;;")); //$NON-NLS-1$
   }

   /**
    * Tests constructor initialization.
    * @throws ParseException if it fails
    */
   @Test
   public void testNewChild() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule(
                  "Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result(), ref("Rule", build("newChild"), append("addChild")))); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
            add(rule("Rule", type(org.fuwjin.test.SampleBuilderPattern.class), _instanceof(), result(), lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("default constructor;adding:new child;;")); //$NON-NLS-1$
   }

   /**
    * Tests pass back finalization.
    * @throws ParseException if it fails
    */
   @Test
   public void testReturn() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule("Grammar", type(), init(), result(), ref("Rule", ignore(), _return()))); //$NON-NLS-1$//$NON-NLS-2$
            add(rule("Rule", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result(), lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("default constructor;")); //$NON-NLS-1$
   }

   /**
    * Tests pass through initialization.
    * @throws ParseException if it fails
    */
   @Test
   public void testThis() throws ParseException {
      final Object obj = new Grammar() {
         {
            add(rule(
                  "Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result(), ref("Rule", _this(), append("addChild")))); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            add(rule("Rule", type(org.fuwjin.test.SampleBuilderPattern.class), _instanceof(), result(), lit('a'))); //$NON-NLS-1$
            resolve();
         }
      }.parse(new StringReader(A));
      assertThat(obj.toString(), is("default constructor;adding:default constructor;;")); //$NON-NLS-1$
   }
}
