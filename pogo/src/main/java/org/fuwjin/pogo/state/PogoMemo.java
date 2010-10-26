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
public interface PogoMemo {
   /**
    * Adds an entry to the failure stack.
    */
   public void fail();

   /**
    * Returns true if this memo has been stored.
    * @return true if stored, false otherwise
    */
   public boolean isStored();

   /**
    * Stores the memo for further retrieval.
    */
   public void store();
}
