package org.fuwjin.test;

import static java.util.Collections.singletonMap;
import static org.fuwjin.gleux.Gleux.newGleux;
import static org.fuwjin.gleux.InStream.streamOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.fuwjin.gleux.Gleux;
import org.fuwjin.gleux.GleuxInterpreter.GleuxException;
import org.junit.Test;

/**
 * Demos highlighting each type of Gleux return.
 */
public class GleuxReturnDemo {
   /**
    * Demo a simple return literal.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoReturn() throws GleuxException {
      final Gleux parser = newGleux("<Root>{return 'value'}");
      assertThat((String)parser.transform(streamOf("value")), is("value"));
      assertThat((String)parser.transform(streamOf("")), is("value"));
   }

   /**
    * Demo a simple return accept.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoReturnAccept() throws GleuxException {
      final Gleux parser = newGleux("<Root>{return accept next}");
      assertThat((Integer)parser.transform(streamOf("a")), is((int)'a'));
      assertThat((Integer)parser.transform(streamOf("bc")), is((int)'b'));
      try {
         parser.transform(streamOf(""));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("EOF"));
      }
   }

   /**
    * Demo a simple return accept in.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoReturnAcceptIn() throws GleuxException {
      final Gleux parser = newGleux("<Root>{return accept in a-z,_}");
      assertThat((Integer)parser.transform(streamOf("a")), is((int)'a'));
      assertThat((Integer)parser.transform(streamOf("bc")), is((int)'b'));
      assertThat((Integer)parser.transform(streamOf("_")), is((int)'_'));
      try {
         parser.transform(streamOf(""));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("EOF"));
      }
      try {
         parser.transform(streamOf("$a"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("match"));
      }
   }

   /**
    * Demo a simple return accept in.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoReturnAcceptNotIn() throws GleuxException {
      final Gleux parser = newGleux("<Root>{return accept not in a-z,_}");
      assertThat((Integer)parser.transform(streamOf("?")), is((int)'?'));
      assertThat((Integer)parser.transform(streamOf("$a")), is((int)'$'));
      try {
         parser.transform(streamOf(""));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("EOF"));
      }
      try {
         parser.transform(streamOf("_"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("match"));
      }
      try {
         parser.transform(streamOf("a"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("match"));
      }
      try {
         parser.transform(streamOf("bc"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("match"));
      }
   }

   /**
    * Demo a simple return accept value.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoReturnAcceptNotValue() throws GleuxException {
      final Gleux parser = newGleux("<Root>{return accept not 'value'}");
      assertThat((Integer)parser.transform(streamOf("val")), is((int)'v'));
      assertThat((Integer)parser.transform(streamOf("valentine")), is((int)'v'));
      try {
         parser.transform(streamOf("value"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("match"));
      }
      try {
         parser.transform(streamOf("values"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("match"));
      }
   }

   /**
    * Demo a simple return accept value.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoReturnAcceptValue() throws GleuxException {
      final Gleux parser = newGleux("<Root>{return accept 'value'}");
      assertThat((Integer)parser.transform(streamOf("value")), is((int)'e'));
      assertThat((Integer)parser.transform(streamOf("values")), is((int)'e'));
      try {
         parser.transform(streamOf("val"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("EOF"));
      }
      try {
         parser.transform(streamOf("valentine"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("character"));
      }
   }

   /**
    * Demo a simple return invocation.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoReturnInvocation() throws GleuxException {
      final Gleux parser = newGleux("<Root>{return java.lang.String.toUpperCase('test')}");
      assertThat((String)parser.transform(streamOf("value")), is("TEST"));
   }

   /**
    * Demo a simple return variable.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoReturnInvocationVariable() throws GleuxException {
      final Gleux parser = newGleux("<Root>{return java.lang.String.toUpperCase(var)}");
      assertThat((String)parser.transform(streamOf("value"), singletonMap("var", "test")), is("TEST"));
      assertThat((String)parser.transform(streamOf(""), singletonMap("var", "value")), is("VALUE"));
      try {
         parser.transform(streamOf("anything"), singletonMap("VAR", "value"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("var is unset"));
      }
      try {
         parser.transform(streamOf(""));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("var is unset"));
      }
   }

   /**
    * Demo a simple return variable.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoReturnVariable() throws GleuxException {
      final Gleux parser = newGleux("<Root>{return var}");
      assertThat((String)parser.transform(streamOf("value"), singletonMap("var", "test")), is("test"));
      assertThat((String)parser.transform(streamOf(""), singletonMap("var", "value")), is("value"));
      try {
         parser.transform(streamOf("anything"), singletonMap("VAR", "value"));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("var is unset"));
      }
      try {
         parser.transform(streamOf(""));
      } catch(final RuntimeException e) {
         assertTrue(e.getMessage().contains("var is unset"));
      }
   }
}
