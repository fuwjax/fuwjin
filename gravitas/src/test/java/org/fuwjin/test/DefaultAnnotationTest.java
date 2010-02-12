package org.fuwjin.test;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Retention;

import org.fuwjin.gravitas.util.ClassUtils;
import org.junit.Test;

@DefaultAnnotationTest.TestAnnotation
public class DefaultAnnotationTest{
   @Test
   public void testToString(){
      TestAnnotation real = DefaultAnnotationTest.class.getAnnotation(TestAnnotation.class);
      TestAnnotation fake = ClassUtils.getAnnotation(String.class, TestAnnotation.class);
      assertThat(real.toString(), is(fake.toString()));
   }
   
   @Test
   public void testHashCode(){
      TestAnnotation real = DefaultAnnotationTest.class.getAnnotation(TestAnnotation.class);
      TestAnnotation fake = ClassUtils.getAnnotation(String.class, TestAnnotation.class);
      assertThat(real.hashCode(), is(fake.hashCode()));
   }
   
   @Test
   public void testEquals(){
      TestAnnotation real = DefaultAnnotationTest.class.getAnnotation(TestAnnotation.class);
      TestAnnotation fake = ClassUtils.getAnnotation(String.class, TestAnnotation.class);
      assertTrue(real.equals(fake));
      assertTrue(fake.equals(real));
   }
   
   @Retention(RUNTIME)
   public @interface TestAnnotation{
      int[] i() default {1};
   }
}
