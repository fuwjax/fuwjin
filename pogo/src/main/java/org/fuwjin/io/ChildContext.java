/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.io;

import org.fuwjin.postage.Postage;

/**
 * The standard child PogoContext.
 */
public class ChildContext implements PogoContext {
   private final PogoRootContext root;
   private Object object;
   private final Position origin;
   private final String name;

   /**
    * Creates a new instance.
    * @param root the root context
    */
   public ChildContext(final String name, final PogoRootContext root) {
      this.name = name;
      this.root = root;
      origin = root.position();
   }

   @Override
   public void accept() {
      root.accept(this);
   }

   @Override
   public void accept(final int ch) {
      root.accept(this, ch);
   }

   @Override
   public void accept(final int start, final int end) {
      root.accept(this, start, end);
   }

   @Override
   public PogoException failedCheck(final String message) {
      return new PogoException("failed test: " + message).label(name, position());
   }

   @Override
   public Object get() {
      return object;
   }

   @Override
   public boolean isSuccess() {
      return root.isSuccess();
   }

   @Override
   public String match() {
      return root.substring(origin.position());
   }

   @Override
   public PogoContext newChild(final String name, final Object newObject, final PogoException failure) {
      return root.newChild(name).set(newObject, failure);
   }

   @Override
   public Position position() {
      return root.position();
   }

   @Override
   public PogoException postageException(final Object result) {
      if(Postage.isSuccess(result)) {
         return null;
      }
      return new PogoException("failed: " + result).label(name, position());
   }

   @Override
   public void seek(final Position mark) {
      root.seek(mark);
   }

   @Override
   public PogoContext set(final Object object, final PogoException failure) {
      success(failure);
      this.object = object;
      return this;
   }

   @Override
   public void success(final PogoException failure) {
      root.success(failure);
   }
}
