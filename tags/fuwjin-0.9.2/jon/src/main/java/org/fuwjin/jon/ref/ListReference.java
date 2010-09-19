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

import java.util.Iterator;

/**
 * Manages the reference to a list.
 */
public class ListReference extends BaseReference implements BaseReference.ListReference {
   private final Iterable<?> value;
   private final ReferenceStorage storage;
   private final Class<?> elementType;

   /**
    * Creates a new instance.
    * @param name the reference name
    * @param storage the storage
    * @param cast the object type
    * @param value the iterable value
    * @param elementType the type of the list elements
    */
   public ListReference(final String name, final ReferenceStorage storage, final Object cast, final Iterable<?> value,
         final Class<?> elementType) {
      super(name, cast);
      this.storage = storage;
      this.value = value;
      this.elementType = elementType;
   }

   Object getValue(final Object next) {
      return storage.get(next, elementType);
   }

   @Override
   public Iterator<Object> iterator() {
      final Iterator<?> iter = value.iterator();
      return new Iterator<Object>() {
         @Override
         public boolean hasNext() {
            return iter.hasNext();
         }

         @Override
         public Object next() {
            return getValue(iter.next());
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }
}
