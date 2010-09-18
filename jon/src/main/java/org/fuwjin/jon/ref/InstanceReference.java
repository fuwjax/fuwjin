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

import org.fuwjin.jon.builder.util.SuperclassIterable;

/**
 * The reference for an instance.
 */
public class InstanceReference extends BaseReference implements Iterable<BaseReference.MapReference> {
   private final ReferenceStorage storage;
   private final Iterable<Class<?>> iterable;
   private final Object value;

   /**
    * Creates a new instance.
    * @param name the reference name
    * @param storage the storage
    * @param type the object type
    * @param value the object
    */
   public InstanceReference(final String name, final ReferenceStorage storage, final Object type, final Object value) {
      super(name, type);
      this.storage = storage;
      this.value = value;
      iterable = new SuperclassIterable(value.getClass());
   }

   @Override
   public Iterator<BaseReference.MapReference> iterator() {
      final Iterator<Class<?>> iter = iterable.iterator();
      return new Iterator<BaseReference.MapReference>() {
         @Override
         public boolean hasNext() {
            return iter.hasNext();
         }

         @Override
         public BaseReference.MapReference next() {
            return newReference(iter.next());
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   ObjectReference newReference(final Class<?> next) {
      return new ObjectReference(storage, next, value);
   }
}
