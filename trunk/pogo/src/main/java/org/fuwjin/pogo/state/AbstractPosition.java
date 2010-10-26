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

import java.util.LinkedList;
import java.util.List;

/**
 * The standard position implementation.
 */
public abstract class AbstractPosition implements PogoPosition {
   private final AbstractState state;
   private final int index;
   private final int line;
   private final int column;
   private final List<AbstractMemo> memos;
   private PogoPosition next;

   /**
    * Creates a new instance from a previous instance.
    * @param previous the previous instance
    * @param shouldBufferNext true if the previous instance should point to this
    *        new instance
    * @param line the new line number
    * @param column the new column number
    */
   public AbstractPosition(final AbstractPosition previous, final boolean shouldBufferNext, final int line,
         final int column) {
      if(shouldBufferNext) {
         previous.next = this;
      } else {
         previous.next = PogoPosition.NULL;
      }
      index = previous.index + 1;
      memos = new LinkedList<AbstractMemo>();
      state = previous.state;
      this.line = line;
      this.column = column;
   }

   /**
    * Creates a new initial instance.
    * @param state the state managing the positions
    */
   public AbstractPosition(final AbstractState state) {
      this.state = state;
      memos = new LinkedList<AbstractMemo>();
      index = 0;
      line = 1;
      column = 1;
   }

   protected void clearNext() {
      next = PogoPosition.NULL;
   }

   protected int column() {
      return column;
   }

   protected AbstractMemo getMemo(final String name) {
      for(final AbstractMemo memo: memos) {
         if(memo.name().equals(name)) {
            return memo;
         }
      }
      return null;
   }

   protected int index() {
      return index;
   }

   protected int line() {
      return line;
   }

   protected PogoPosition next() {
      return next;
   }

   @Override
   public void release() {
      state.release();
   }

   @Override
   public void reset() {
      state.set(this);
   }

   protected void addMemo(final AbstractMemo memo) {
      memos.add(memo);
   }

   @Override
   public String toString() {
      return "[" + line + "," + column + "]";
   }
}
