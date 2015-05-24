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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.fuwjin.util.AtomicIterable;
import org.fuwjin.util.AtomicIterator;
import org.fuwjin.util.FailureAssertion;
import org.junit.Test;

/**
 * Exercises the atomic iterable and iterator.
 */
public class AtomicIterableIteratorTest{
   /**
    * Negative lengths are not allowed for the atomic iterable.
    */
   @Test(expected = Throwable.class)
   public void shouldFailOnNegativeLength(){
      new AtomicIterable<Object>(-1);
   }

   /**
    * Next cannot be called with no more elements.
    */
   @Test
   public void shouldFailOnNextAfterEnd(){
      final AtomicIterator<Object> iterator = new AtomicIterable<Object>(0).iterator();
      new FailureAssertion(){
         @Override
         public void whenDoing() throws Throwable{
            iterator.next();
         }
      }.shouldThrow(NoSuchElementException.class);
   }

   /**
    * Set cannot be called before next.
    */
   @Test
   public void shouldFailOnSetBeforeNext(){
      final AtomicIterator<Object> iterator = new AtomicIterable<Object>(10).iterator();
      new FailureAssertion(){
         @Override
         public void whenDoing() throws Throwable{
            iterator.replace(17);
         }
      }.shouldThrow(IndexOutOfBoundsException.class);
   }

   /**
    * This is the basic behavior for an iterable.
    */
   @Test
   public void shouldPerformAsIterable(){
      final AtomicIterable<Object> iterable = new AtomicIterable<Object>(10);
      int count = 0;
      final Iterator<Object> iter = iterable.iterator();
      while(iter.hasNext()){
         iter.next();
         count++;
      }
      assertThat(count, is(10));
   }

   /**
    * Remove should set the current position to null.
    */
   @Test
   public void shouldRemoveBySettingNull(){
      final AtomicIterable<String> iterable = new AtomicIterable<String>(10);
      AtomicIterator<String> iter = iterable.iterator();
      iter.next();
      iter.replace("Bob");
      iter = iterable.iterator();
      assertThat(iter.next(), is("Bob"));
      iter.remove();
      iter = iterable.iterator();
      assertThat(iter.next(), nullValue());
   }
}
