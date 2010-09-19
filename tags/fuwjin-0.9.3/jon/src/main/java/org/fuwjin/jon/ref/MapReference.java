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
import java.util.Map;

/**
 * Manages the reference for the map.
 */
public class MapReference extends BaseReference implements BaseReference.MapReference {
   private final Map<?, ?> value;
   private final ReferenceStorage storage;
   private final Class<?> keyType;
   private final Class<?> valueType;

   /**
    * Creates a new instance.
    * @param name the reference name
    * @param storage the storage
    * @param type the map type
    * @param value the map instance
    * @param keyType the key type
    * @param valueType the value type
    */
   public MapReference(final String name, final ReferenceStorage storage, final Object type, final Map<?, ?> value,
         final Class<?> keyType, final Class<?> valueType) {
      super(name, type);
      this.storage = storage;
      this.value = value;
      this.keyType = keyType;
      this.valueType = valueType;
   }

   @Override
   public Iterator<EntryReference> iterator() {
      final Iterator<? extends Map.Entry<?, ?>> iter = value.entrySet().iterator();
      return new Iterator<EntryReference>() {
         @Override
         public boolean hasNext() {
            return iter.hasNext();
         }

         @Override
         public EntryReference next() {
            return newEntry(iter.next());
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   EntryReference newEntry(final Map.Entry<?, ?> entry) {
      final Object key = storage.get(entry.getKey(), keyType);
      final Object val = storage.get(entry.getValue(), valueType);
      return new EntryReference(key, val);
   }
}
