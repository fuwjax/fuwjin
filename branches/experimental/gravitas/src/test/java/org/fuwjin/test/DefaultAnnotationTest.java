package org.fuwjin.test;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Retention;
import java.util.Arrays;

import org.fuwjin.util.ClassUtils;
import org.junit.Test;

@DefaultAnnotationTest.TestAnnotation(val = 0, types = {})
public class DefaultAnnotationTest{
   @Retention(RUNTIME)
   public @interface NullAnnotation{
      Class<?> type();
   }

   @Retention(RUNTIME)
   public @interface TestAnnotation{
      byte[] b() default {1};

      char[] c() default {'1'};

      double[] d() default {1};

      float[] f() default {1};

      int[] i() default {1};

      long[] l() default {1};

      short[] s() default {1};

      boolean[] t() default {true};

      Class<?>[] types();

      int val();
   }

   private static final String PREFIX = "@" + TestAnnotation.class.getName() + "(";

   @Test
   public void testAnnotationType(){
      final TestAnnotation real = DefaultAnnotationTest.class.getAnnotation(TestAnnotation.class);
      final TestAnnotation fake = ClassUtils.getAnnotation(String.class, TestAnnotation.class);
      assertEquals(real.annotationType(), fake.annotationType());
   }

   @Test
   public void testEquals(){
      final TestAnnotation real = DefaultAnnotationTest.class.getAnnotation(TestAnnotation.class);
      final TestAnnotation fake = ClassUtils.getAnnotation(String.class, TestAnnotation.class);
      assertTrue(real.equals(fake));
      assertTrue(fake.equals(real));
   }

   @Test
   public void testEqualsItselfWithNull(){
      final NullAnnotation fake1 = ClassUtils.getAnnotation(String.class, NullAnnotation.class);
      final NullAnnotation fake2 = ClassUtils.getAnnotation(String.class, NullAnnotation.class);
      assertTrue(fake1.equals(fake2));
   }

   @Test
   public void testHashCode(){
      final TestAnnotation real = DefaultAnnotationTest.class.getAnnotation(TestAnnotation.class);
      final TestAnnotation fake = ClassUtils.getAnnotation(String.class, TestAnnotation.class);
      assertThat(real.hashCode(), is(fake.hashCode()));
   }

   @Test
   public void testNullHashCode(){
      final NullAnnotation fake = ClassUtils.getAnnotation(String.class, NullAnnotation.class);
      assertThat(fake.hashCode(), is(127 * "type".hashCode()));
   }

   @Test
   public void testNullToString(){
      final NullAnnotation fake = ClassUtils.getAnnotation(String.class, NullAnnotation.class);
      assertThat(fake.toString(), is("@" + NullAnnotation.class.getName() + "(type=null)"));
   }

   @Test
   public void testNullValue(){
      @NullAnnotation(type = Object.class)
      class DifferentAnnotation{
      }
      final NullAnnotation real = DifferentAnnotation.class.getAnnotation(NullAnnotation.class);
      final NullAnnotation fake = ClassUtils.getAnnotation(String.class, NullAnnotation.class);
      assertFalse(real.equals(fake));
      assertFalse(fake.equals(real));
   }

   @Test
   public void testToString(){
      final TestAnnotation real = DefaultAnnotationTest.class.getAnnotation(TestAnnotation.class);
      final TestAnnotation fake = ClassUtils.getAnnotation(String.class, TestAnnotation.class);
      assertThat(sort(split(strip(real.toString()))), is(sort(split(strip(fake.toString())))));
   }

   @Test
   public void testUnequalType(){
      @NullAnnotation(type = Object.class)
      class DifferentAnnotation{
      }
      final NullAnnotation real = DifferentAnnotation.class.getAnnotation(NullAnnotation.class);
      final TestAnnotation fake = ClassUtils.getAnnotation(String.class, TestAnnotation.class);
      assertFalse(real.equals(fake));
      assertFalse(fake.equals(real));
   }

   @Test
   public void testUnequalValues(){
      @TestAnnotation(val = 1, types = {})
      class NonDefaultAnnotation{
      }
      final TestAnnotation real = NonDefaultAnnotation.class.getAnnotation(TestAnnotation.class);
      final TestAnnotation fake = ClassUtils.getAnnotation(String.class, TestAnnotation.class);
      assertFalse(real.equals(fake));
      assertFalse(fake.equals(real));
   }

   private String[] sort(final String[] split){
      Arrays.sort(split);
      return split;
   }

   private String[] split(final String strip){
      return strip.split(", ");
   }

   private String strip(final String string){
      assertTrue(string.startsWith(PREFIX));
      assertTrue(string.endsWith(")"));
      return string.substring(PREFIX.length(), string.length() - 1);
   }
}
