/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.pogo.state;

import org.fuwjin.pogo.PogoException;
import org.fuwjin.postage.Failure;

/**
 * The standard state implementation.
 */
public abstract class AbstractState implements PogoState {
   private Object value;
   private AbstractPosition current;
   private int marks;
   private int memos;
   private final PogoFailure failure = new PogoFailure();

   @Override
   public AbstractPosition current() {
      return current;
   }

   @Override
   public PogoException exception() {
      return failure.exception();
   }

   protected void fail(final int start, final int end) {
      failure.fail(current, start, end);
   }

   @Override
   public void fail(final String string, final Failure cause) {
      failure.fail(current, string, cause);
   }

   protected void failStack(final String name) {
      releaseMemo();
      failure.failStack(current, name, memos);
   }

   @Override
   public PogoMemo getMemo(final String name) {
      memos++;
      AbstractMemo memo = current.getMemo(name);
      if(memo == null) {
         memo = newMemo(name);
      } else {
         memo.restore();
      }
      return memo;
   }

   @Override
   public Object getValue() {
      return value;
   }

   @Override
   public boolean isAfter(final PogoPosition mark) {
      return current.index() > ((AbstractPosition)mark).index();
   }

   @Override
   public AbstractPosition mark() {
      marks++;
      return current;
   }

   protected abstract AbstractMemo newMemo(String name);

   protected void release() {
      marks--;
   }

   protected void releaseMemo() {
      memos--;
   }

   protected void set(final AbstractPosition pos) {
      current = pos;
   }

   @Override
   public void setValue(final Object object) {
      value = object;
   }

   protected boolean shouldBufferNext() {
      return marks > 0;
   }
}
