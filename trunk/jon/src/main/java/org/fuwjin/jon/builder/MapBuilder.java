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
package org.fuwjin.jon.builder;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.MapReference;
import org.fuwjin.jon.ref.ReferenceStorage;

/**
 * Builds a map.
 */
public class MapBuilder extends EntriesBuilder {
   /**
    * Creates a new instance.
    */
   public MapBuilder() {
      super(HashMap.class);
   }

   /**
    * Creates a new instance.
    * @param type the map type
    */
   public MapBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public EntryBuilder newEntry() {
      return new EntryBuilder() {
         private Map<Object, Object> map() {
            return (Map<Object, Object>)target;
         }

         @Override
         public Builder newKey() {
            return null;
         }

         @Override
         public Builder newValue() {
            return null;
         }

         @Override
         public void storeImpl() {
            map().put(key, value);
         }
      };
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return new MapReference(storage.nextName(), storage, cast(storage, obj, cls), (Map<?, ?>)obj, null, null);
   }
}
