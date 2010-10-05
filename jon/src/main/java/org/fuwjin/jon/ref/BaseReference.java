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
package org.fuwjin.jon.ref;

/**
 * The base class for references.
 */
public abstract class BaseReference {
   /**
    * The base interface for list references.
    */
   public abstract static interface ListReference extends Iterable<Object> {
      // for consistency
   }

   /**
    * The base interface for map references.
    */
   public abstract static interface MapReference extends Iterable<EntryReference> {
      // for consistency
   }

   private final String name;
   private final Object type;
   private boolean justReference;

   protected BaseReference(final String name, final Object type) {
      this.type = type;
      this.name = name;
   }

   /**
    * Returns true if this reference is type cast.
    * @return true if a cast, false otherwise
    */
   public boolean isCast() {
      return type != null;
   }

   /**
    * Returns true if the cast should be omitted.
    * @return true if just a reference, false otherwise
    */
   public boolean isJustReference() {
      return justReference;
   }

   /**
    * Returns true if the reference is named.
    * @return true if this is a reference, false otherwise
    */
   public boolean isReference() {
      return name != null;
   }

   protected void queuedForWriting() {
      justReference = true;
   }
}
