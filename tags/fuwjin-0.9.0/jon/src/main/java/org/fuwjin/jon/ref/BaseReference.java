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

public abstract class BaseReference {
   public abstract static interface ListReference extends Iterable<Object> {
   }

   public abstract static interface MapReference extends Iterable<EntryReference> {
   }

   private final String name;
   private final Object type;
   private boolean justReference;

   protected BaseReference(final String name, final Object type) {
      this.type = type;
      this.name = name;
   }

   public boolean isCast() {
      return type != null;
   }

   public boolean isJustReference() {
      return justReference;
   }

   public boolean isReference() {
      return name != null;
   }

   protected void queuedForWriting() {
      justReference = true;
   }
}
