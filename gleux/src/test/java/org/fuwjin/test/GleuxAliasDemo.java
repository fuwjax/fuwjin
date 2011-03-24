package org.fuwjin.test;

import static org.fuwjin.gleux.Gleux.newGleux;
import static org.fuwjin.gleux.InStream.streamOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjin.gleux.Gleux;
import org.fuwjin.gleux.GleuxInterpreter.GleuxException;
import org.junit.Test;

/**
 * Demos the use of the alias keyword.
 */
public class GleuxAliasDemo {
   /**
    * Demo an aliased class.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoAliasClass() throws GleuxException {
      final Gleux parser = newGleux("alias java.lang.String as S <Root>{return S.toUpperCase('value')}");
      assertThat((String)parser.transform(streamOf("")), is("VALUE"));
   }

   /**
    * Demo an aliased method.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoAliasMethod() throws GleuxException {
      final Gleux parser = newGleux("alias java.lang.String.toUpperCase as upper <Root>{return upper('value')}");
      assertThat((String)parser.transform(streamOf("")), is("VALUE"));
   }

   /**
    * Demo an aliased package.
    * @throws GleuxException if it fails
    */
   @Test
   public void demoAliasPackage() throws GleuxException {
      final Gleux parser = newGleux("alias java.lang as jl <Root>{return jl.String.toUpperCase('value')}");
      assertThat((String)parser.transform(streamOf("")), is("VALUE"));
   }
}
