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

import static org.fuwjin.jon.BuilderRegistry.getBuilder;

import java.util.IdentityHashMap;

/**
 * Stores references.
 */
public class ReferenceStorage {
   private final IdentityHashMap<Object, BaseReference> map = new IdentityHashMap<Object, BaseReference>();
   private int index;

   /**
    * Returns a reference.
    * @param obj the referenced value
    * @param type the value type
    * @return the reference
    */
   public Object get(final Object obj, final Class<?> type) {
      if(obj == null) {
         return null;
      }
      BaseReference ref = map.get(obj);
      if(ref == null) {
         ref = getBuilder(obj.getClass()).newReference(obj, type, this);
         if(ref == null) {
            return obj;
         }
         map.put(obj, ref);
      } else {
         ref.queuedForWriting();
      }
      return ref;
   }

   /**
    * Returns the next reference name.
    * @return the next name
    */
   public String nextName() {
      return Integer.toString(index++);
   }
}
