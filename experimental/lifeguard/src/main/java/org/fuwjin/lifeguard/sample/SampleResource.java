/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.lifeguard.sample;

import static java.lang.Thread.sleep;

import java.util.Random;
import java.util.concurrent.Callable;

import org.fuwjin.lifeguard.Resource;
import org.fuwjin.lifeguard.ResourceTracker;

/**
 * A sample pooled object for testing.
 */
public class SampleResource implements Resource, Callable<Object> {
   private static final int ABANDON_FRACTION = 3;
   private final Random rand;
   private boolean closed;
   private ResourceTracker<?> tracker;

   protected SampleResource(final Random rand) {
      this.rand = rand;
   }

   @Override
   public Object call() throws Exception {
      final int number = rand.nextInt(100);
      sleep(number);
      if(number % ABANDON_FRACTION == 0) {
         tracker.abandon();
      } else {
         tracker.release();
      }
      return null;
   }

   @Override
   public void close() {
      closed = true;
   }

   public boolean isClosed() {
      return closed && tracker.isClosed();
   }

   @Override
   public boolean isValid() throws Exception {
      return true;
   }

   @Override
   public void setTracker(final ResourceTracker<?> tracker) {
      this.tracker = tracker;
   }
}
