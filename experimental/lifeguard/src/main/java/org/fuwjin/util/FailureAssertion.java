/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.util;

import static java.lang.String.format;

/**
 * Allows exception throwing tests in ways that preserve coverage measurement.
 */
public abstract class FailureAssertion{
   private static final String NO_EXCEPTION = "expected %s but no exception was thrown"; //$NON-NLS-1$
   private static final String UNEXPECTED_EXCEPTION = "expected %s but %s was thrown";

   /**
    * Asserts that the implementation of whenDoing() throws the expected
    * exception.
    * @param expected the expected exception
    */
   public final void shouldThrow(final Class<? extends Throwable> expected){
      try{
         whenDoing();
      }catch(final AssertionError e){
         return; // assume it would have thrown the right exception?
      }catch(final Throwable e){
         if(expected.equals(e.getClass())){
            return;
         }
         throw (AssertionError)new AssertionError(format(UNEXPECTED_EXCEPTION, expected, e.getClass())).initCause(e);
      }
      throw new AssertionError(format(NO_EXCEPTION, expected));
   }

   /**
    * Extentions to this class should override this method with the minimum
    * instructions to throw the desired exception.
    * @throws Throwable an exception should be thrown when this method is called
    */
   public abstract void whenDoing() throws Throwable;
}
