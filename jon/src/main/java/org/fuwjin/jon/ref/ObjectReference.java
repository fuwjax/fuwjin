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

import java.lang.reflect.Field;
import java.util.Iterator;

import org.fuwjin.jon.builder.util.SerializableFieldIterable;
import org.fuwjin.jon.ref.BaseReference.MapReference;

/**
 * Manages an object reference.
 */
public class ObjectReference implements MapReference {
   private final Iterable<Field> type;
   private final Object value;
   private final ReferenceStorage storage;

   /**
    * Creates a new instance.
    * @param storage the storage
    * @param type the class type
    * @param value the object value
    */
   public ObjectReference(final ReferenceStorage storage, final Class<?> type, final Object value) {
      this.storage = storage;
      this.type = new SerializableFieldIterable(type);
      this.value = value;
   }

   @Override
   public Iterator<EntryReference> iterator() {
      final Iterator<Field> iter = type.iterator();
      return new Iterator<EntryReference>() {
         @Override
         public boolean hasNext() {
            return iter.hasNext();
         }

         @Override
         public EntryReference next() {
            return newReference(iter.next());
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   EntryReference newReference(final Field field) {
      try {
         field.setAccessible(true);
         final Object val = storage.get(field.get(value), field.getType());
         return new EntryReference(field, val);
      } catch(final IllegalAccessException e) {
         throw new RuntimeException(e);
      }
   }
}