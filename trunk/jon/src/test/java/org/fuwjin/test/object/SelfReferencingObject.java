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
package org.fuwjin.test.object;

/**
 * A simple self-referencing class
 * @author michaeldoberenz
 */
public class SelfReferencingObject {
   private final SelfReferencingObject obj;

   /**
    * creates a new self-referencing object
    */
   public SelfReferencingObject() {
      obj = this;
   }

   @Override
   public boolean equals(final Object object) {
      try {
         final SelfReferencingObject o = (SelfReferencingObject)object;
         return o.obj == obj;
      } catch(final RuntimeException e) {
         return false;
      }
   }

   /**
    * Returns the self reference.
    * @return the self reference
    */
   public Object getSelf() {
      return obj;
   }

   @Override
   public int hashCode() {
      return System.identityHashCode(obj);
   }
}
