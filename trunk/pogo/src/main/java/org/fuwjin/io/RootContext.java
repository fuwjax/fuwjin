/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.io;

/**
 * A base PogoContext for managing parse state.
 */
public abstract class RootContext implements PogoRootContext {
   private PogoException failure = null;

   /**
    * Creates a new instance.
    * @param object the initial context object, may be null
    */
   protected RootContext() {
      failure = null;
   }

   @Override
   public void assertSuccess() throws PogoException {
      if(failure != null) {
         throw failure;
      }
   }

   @Override
   public abstract boolean hasRemaining();

   @Override
   public boolean isSuccess() {
      return failure == null;
   }

   /**
    * Creates a new child context under this root.
    * @return the new child context
    */
   @Override
   public abstract PogoContext newChild(String name);

   /**
    * Returns the substring of this context from start to the current position.
    * @param start the start of the substring
    * @return the substring
    */
   @Override
   public abstract String substring(int start);

   @Override
   public void success(final PogoException failure) {
      if(failure != null && this.failure != null) {
         if(this.failure.position() < failure.position()) {
            return;
         }
         failure.initCause(this.failure);
      }
      this.failure = failure;
   }
}
