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

/**
 * The memo structure for a pogo state.
 */
public class AbstractMemo implements PogoMemo {
   private Object value;
   private final AbstractState state;
   private final AbstractPosition start;
   private AbstractPosition end;
   private final String name;

   /**
    * Creates a new instance.
    * @param name the rule name
    * @param state the state of the parse
    */
   public AbstractMemo(final String name, final AbstractState state) {
      this.name = name;
      this.state = state;
      start = state.current();
   }

   /**
    * Adds an entry to the failure stack.
    */
   @Override
   public void fail() {
      state.failStack(name);
   }

   /**
    * Returns true if this memo has been stored.
    * @return true if stored, false otherwise
    */
   @Override
   public boolean isStored() {
      return end != null;
   }

   protected String name() {
      return name;
   }

   protected void restore() {
      state.set(end);
      state.setValue(value);
   }

   /**
    * Stores the memo for further retrieval.
    */
   @Override
   public void store() {
      value = state.getValue();
      end = state.current();
      start.addMemo(this);
      state.releaseMemo();
   }
}
