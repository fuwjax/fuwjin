/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import org.fuwjin.lifeguard.LifeGuard;
import org.fuwjin.lifeguard.sample.SampleResource;
import org.fuwjin.lifeguard.sample.SampleResourceFactory;
import org.fuwjin.util.FailureAssertion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the object pool.
 */
public class WhenPoolingObjects {
   private LifeGuard<SampleResource> manager;
   private SampleResourceFactory factory;

   /**
    * Disposes the test system.
    * @throws Exception if the setup fails
    */
   @After
   public void allYourObjectAreBelongToUs() throws Exception {
      manager.close(1, SECONDS);
      assertTrue(factory.isClosed());
   }

   /**
    * The exception from getImpl should be propagated.
    */
   @Test
   public void shouldNotPassOnExceptionFromGetImpl() {
      factory = new SampleResourceFactory(null);
      manager = new LifeGuard<SampleResource>(factory, 5);
      new FailureAssertion() {
         @Override
         public void whenDoing() throws Throwable {
            manager.get(1, SECONDS);
         }
      }.shouldThrow(TimeoutException.class);
   }

   /**
    * Tests that the manager can hand out only active instances.
    * @throws Exception if the test fails
    */
   @Test
   public void shouldRunLotsOfTimes() throws Exception {
      for(int i = 0; i < 10; i++) {
         manager.get(1, SECONDS).call();
      }
   }

   /**
    * Should allow interrupt and throw an interrupted exception.
    * @throws Exception when interrupted
    */
   @Test(expected = InterruptedException.class)
   public void shouldThrowInterrupted() throws Exception {
      final Thread thread = Thread.currentThread();
      new Timer().schedule(new TimerTask() {
         @Override
         public void run() {
            thread.interrupt();
         }
      }, 10);
      for(int i = 0; i < 10; i++) {
         manager.get(1, SECONDS).call();
      }
   }

   /**
    * Should throw a timeout if an available object is not found.
    * @throws Exception when timed out
    */
   @Test(expected = TimeoutException.class)
   public void shouldTimeoutIfNoObjectIsAvailable() throws Exception {
      for(int i = 0; i < 10; i++) {
         manager.get(1, MILLISECONDS).call();
      }
   }

   /**
    * Should throw a timeout if the close doesn't finish in time.
    * @throws Exception if the test fails
    */
   @Test
   public void shouldTimeoutOnClose() throws Exception {
      for(int i = 0; i < 10; i++) {
         manager.get(1, SECONDS).call();
      }
      new FailureAssertion() {
         @Override
         public void whenDoing() throws Throwable {
            manager.close(1, MILLISECONDS);
         }
      }.shouldThrow(TimeoutException.class);
      // does nothing, close only works the first time
      manager.close(1, MILLISECONDS);
      // closeNow always attempts to shut everyone down immediately
      manager.closeNow();
      // get throws an illegal state after close
      new FailureAssertion() {
         @Override
         public void whenDoing() throws Throwable {
            manager.get(1, MILLISECONDS);
         }
      }.shouldThrow(IllegalStateException.class);
   }

   /**
    * The setup for the system under test.
    */
   @Before
   public void someoneSetUpUsTheTest() {
      factory = new SampleResourceFactory(new Random());
      manager = new LifeGuard<SampleResource>(factory, 5);
   }
}
