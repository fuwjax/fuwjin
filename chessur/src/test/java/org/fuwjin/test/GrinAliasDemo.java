/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.test;

import static org.fuwjin.chessur.Grin.newGrin;
import static org.fuwjin.chessur.InStream.streamOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjin.chessur.ChessurInterpreter.ChessurException;
import org.fuwjin.chessur.Grin;
import org.junit.Test;

/**
 * Demos the use of the alias keyword.
 */
public class GrinAliasDemo {
   /**
    * Demo an aliased class.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoAliasClass() throws ChessurException {
      final Grin parser = newGrin("alias java.lang.String as S <Root>{return S.toUpperCase('value')}");
      assertThat((String)parser.transform(streamOf("")), is("VALUE"));
   }

   /**
    * Demo an aliased method.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoAliasMethod() throws ChessurException {
      final Grin parser = newGrin("alias java.lang.String.toUpperCase as upper <Root>{return upper('value')}");
      assertThat((String)parser.transform(streamOf("")), is("VALUE"));
   }

   /**
    * Demo an aliased package.
    * @throws ChessurException if it fails
    */
   @Test
   public void demoAliasPackage() throws ChessurException {
      final Grin parser = newGrin("alias java.lang as jl <Root>{return jl.String.toUpperCase('value')}");
      assertThat((String)parser.transform(streamOf("")), is("VALUE"));
   }
}
