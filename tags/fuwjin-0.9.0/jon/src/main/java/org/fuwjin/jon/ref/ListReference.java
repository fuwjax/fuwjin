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

public class ListReference extends BaseReference implements BaseReference.ListReference {
   private final Iterable<?> value;
   private final ReferenceStorage storage;
   private final Class<?> elementType;

   public ListReference(final String name, final ReferenceStorage storage, final Object cast, final Iterable<?> value,
         final Class<?> elementType) {
      super(name, cast);
      this.storage = storage;
      this.value = value;
      this.elementType = elementType;
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

   Object getValue(final Object next) {
      return storage.get(next, elementType);
   }
}