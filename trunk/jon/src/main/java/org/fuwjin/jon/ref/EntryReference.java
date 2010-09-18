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
package org.fuwjin.jon.ref;

/**
 * Manages a reference for an entry.
 */
public class EntryReference {
   protected final Object key;
   protected final Object value;

   /**
    * Creates a new instance.
    * @param key the entry key
    * @param val the entry value
    */
   public EntryReference(final Object key, final Object val) {
      this.key = key;
      value = val;
   }
}
