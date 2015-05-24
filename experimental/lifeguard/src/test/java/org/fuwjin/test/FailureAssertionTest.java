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
package org.fuwjin.test;

import org.fuwjin.util.FailureAssertion;
import org.junit.Test;

/**
 * Tests {@link FailureAssertion} behavior.
 */
public class FailureAssertionTest{
   /**
    * Tests FailureAssertion correct behavior.
    */
   @Test
   public void shouldAssertThrownException(){
      new FailureAssertion(){
         @Override
         public void whenDoing() throws Throwable{
            throw new UnsupportedOperationException();
         }
      }.shouldThrow(UnsupportedOperationException.class);
   }

   /**
    * Tests FailureAssertion fails when the error is wrong.
    */
   @Test
   public void shouldFailOnDifferentException(){
      final FailureAssertion assertion = new FailureAssertion(){
         @Override
         public void whenDoing() throws Throwable{
            throw new NoSuchMethodException();
         }
      };
      new FailureAssertion(){
         @Override
         public void whenDoing() throws Throwable{
            assertion.shouldThrow(RuntimeException.class);
         }
      }.shouldThrow(AssertionError.class);
   }

   /**
    * Tests FailureAssertion fails when the expected error succeeds.
    */
   @Test
   public void shouldFailOnSuccess(){
      final FailureAssertion assertion = new FailureAssertion(){
         @Override
         public void whenDoing() throws Throwable{
            // succeed
         }
      };
      new FailureAssertion(){
         @Override
         public void whenDoing() throws Throwable{
            assertion.shouldThrow(RuntimeException.class);
         }
      }.shouldThrow(AssertionError.class);
   }
}
