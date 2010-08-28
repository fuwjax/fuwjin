package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Observable;

import org.fuwjin.postage.Failure.FailureException;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;
import org.fuwjin.sample.SampleObject;
import org.fuwjin.sample.TrivialInterface;
import org.junit.Before;
import org.junit.Test;

public class WhenPostingToClassMethods {
   private Postage postage;

   @Before
   public void setup() {
      postage = new Postage();
   }

   @Test
   public void shouldCurry() throws FailureException {
      final SampleObject object = new SampleObject();
      final Function func = postage.getFunction(TrivialInterface.class.getCanonicalName(), "update");
      func.curry(object).curry(new Observable()).invoke("test");
      assertThat((String)object.updated, is("test"));
   }

   @Test
   public void shouldFailPostIncorrectArgsMessages() throws FailureException {
      final SampleObject object = new SampleObject();
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "sample");
      try {
         func.invoke(object, 7);
         fail("should fail");
      } catch(final FailureException e) {
         // pass();
      }
   }

   @Test
   public void shouldFailPostIncorrectMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "field");
      try {
         func.invoke(7);
         fail("should fail");
      } catch(final FailureException e) {
         // pass();
      }
   }

   @Test
   public void shouldFailPostInvalidArgsCastMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "instanceof");
      assertTrue(!(Boolean)func.invoke(7));
   }

   @Test
   public void shouldFailPostInvalidArgsCreateMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "new");
      try {
         func.invoke(7);
         fail("should fail");
      } catch(final FailureException e) {
         // pass();
      }
   }

   @Test
   public void shouldFailPostInvalidCastMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "instanceof");
      try {
         func.invoke(7, 7);
         fail("should fail");
      } catch(final FailureException e) {
         // pass();
      }
   }

   @Test
   public void shouldFailPostInvalidCreateMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "new");
      try {
         func.invoke(7, 7);
         fail("should fail");
      } catch(final FailureException e) {
         // pass();
      }
   }

   @Test
   public void shouldFailPostMessageFail() throws FailureException {
      final SampleObject object = new SampleObject();
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "fails");
      try {
         func.invoke(object);
         fail("should fail");
      } catch(final FailureException e) {
         // pass();
      }
   }

   @Test
   public void shouldFailPostSetMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "staticField");
      try {
         func.invoke(7);
         fail("should fail");
      } catch(final FailureException e) {
         // pass();
      }
   }

   @Test
   public void shouldFailPostTooManyArgsMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "staticField");
      try {
         func.invoke(7, 7);
         fail("should fail");
      } catch(final FailureException e) {
         // pass();
      }
   }

   @Test
   public void shouldFailPostUnknownMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "doesNotExist");
      try {
         func.invoke("test");
         fail("should fail");
      } catch(final FailureException e) {
         // pass();
      }
   }

   @Test
   public void shouldFailUnknownCategory() throws FailureException {
      try {
         postage.getFunction("doesNotExist", "doesNotExist");
         fail("should fail");
      } catch(final IllegalArgumentException e) {
         // pass();
      }
   }

   @Test
   public void shouldPostCastMessages() throws FailureException {
      final SampleObject object = new SampleObject();
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "instanceof");
      assertThat((Boolean)func.invoke(object), is(true));
   }

   @Test
   public void shouldPostCreateMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "new");
      assertThat(func.invoke(), is(SampleObject.class));
   }

   @Test
   public void shouldPostFieldMessages() throws FailureException {
      final SampleObject object = new SampleObject();
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "field");
      assertNull(func.invoke(object));
      assertNull(func.invoke(object, "test"));
      assertThat((String)func.invoke(object), is("test"));
   }

   @Test
   public void shouldPostMessagesByInterface() throws FailureException {
      final SampleObject object = new SampleObject();
      final Function func = postage.getFunction(TrivialInterface.class.getCanonicalName(), "update");
      func.invoke(object, null, "test");
      assertThat((String)object.updated, is("test"));
   }

   @Test
   public void shouldPostNoArgMessages() throws FailureException {
      final SampleObject object = new SampleObject();
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "sample");
      assertThat((String)func.invoke(object), is("test"));
   }

   @Test
   public void shouldPostStaticFieldMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "staticField");
      assertNull(func.invoke());
      assertNull(func.invoke("test"));
      assertThat((String)func.invoke(), is("test"));
   }

   @Test
   public void shouldPostStaticMessages() throws FailureException {
      final Function func = postage.getFunction(SampleObject.class.getCanonicalName(), "sampleStatic");
      assertThat((String)func.invoke("test"), is("test"));
   }
}
