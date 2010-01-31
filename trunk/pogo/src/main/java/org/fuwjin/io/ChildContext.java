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
package org.fuwjin.io;

import java.text.ParseException;

/**
 * The standard child PogoContext.
 */
public class ChildContext implements PogoContext {
   private final RootContext root;
   private Object object;
   private final int origin;

   /**
    * Creates a new instance.
    * @param root the root context
    */
   public ChildContext(final RootContext root) {
      this.root = root;
      origin = root.position();
   }

   @Override
   public void accept() {
      root.accept();
   }

   @Override
   public void accept(final int ch) {
      root.accept(ch);
   }

   @Override
   public void accept(final int start, final int end) {
      root.accept(start, end);
   }

   @Override
   public void assertSuccess() throws ParseException {
      root.assertSuccess();
   }

   @Override
   public Object get() {
      return object;
   }

   @Override
   public boolean hasRemaining() {
      return root.hasRemaining();
   }

   @Override
   public boolean isSuccess() {
      return root.isSuccess();
   }

   @Override
   public String match() {
      return root.substring(origin);
   }

   @Override
   public PogoContext newChild(final Object newObject, final boolean newSuccess, final String failureReason) {
      return root.newChild().set(newObject, newSuccess, failureReason);
   }

   @Override
   public int position() {
      return root.position();
   }

   @Override
   public void seek(final int mark) {
      root.seek(mark);
   }

   @Override
   public PogoContext set(final Object object, final boolean success, final String failureReason) {
      success(success, failureReason);
      this.object = object;
      return this;
   }

   @Override
   public boolean success(final boolean state, final String failureReason) {
      return root.success(state, failureReason);
   }
}
