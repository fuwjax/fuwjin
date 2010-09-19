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
package org.fuwjin.jon.builder;

import org.fuwjin.jon.container.ContainerProxy;
import org.fuwjin.jon.container.ContainerProxy.ResolveProxyTask;

/**
 * Builds a map.
 */
public abstract class EntriesBuilder extends Builder {
   /**
    * Builds a map entry.
    */
   public static abstract class EntryBuilder {
      protected Object key;
      protected Object value;
      protected Object target;

      private void finish() {
         if(target == null || key instanceof ContainerProxy || value instanceof ContainerProxy) {
            return;
         }
         storeImpl();
      }

      /**
       * Sets the key for the entry.
       * @param newKey the new key
       */
      public void key(final Object newKey) {
         key = newKey;
         if(newKey instanceof ContainerProxy) {
            ((ContainerProxy)newKey).addTask(new ResolveProxyTask() {
               @Override
               public void resolve(final Object resolved) {
                  key(resolved);
               }
            });
         }
         finish();
      }

      /**
       * Returns the builder for the entry key.
       * @return the key builder
       */
      public abstract Builder newKey();

      /**
       * Returns the builder for the entry value.
       * @return the value builder
       */
      public abstract Builder newValue();

      /**
       * Stores the entry on the map.
       * @param map the map
       */
      public void store(final Object map) {
         target = map;
         finish();
      }

      /**
       * Performs the storage after all references have been resolved.
       */
      public abstract void storeImpl();

      /**
       * Sets the value for the entry.
       * @param newValue the new value
       */
      public void value(final Object newValue) {
         value = newValue;
         if(newValue instanceof ContainerProxy) {
            ((ContainerProxy)newValue).addTask(new ResolveProxyTask() {
               @Override
               public void resolve(final Object resolved) {
                  value(resolved);
               }
            });
         }
         finish();
      }
   }

   protected Object map;

   /**
    * Creates a new instance.
    * @param type the map type
    */
   public EntriesBuilder(final Class<?> type) {
      super(type);
      map = newInstance();
   }

   /**
    * Creates a new instance.
    * @param type the map type
    * @param map the map instance
    */
   public EntriesBuilder(final Class<?> type, final Object map) {
      super(type);
      this.map = map;
   }

   /**
    * Adds a new entry.
    * @param entry the entry
    */
   public void add(final EntryBuilder entry) {
      entry.store(map);
   }

   /**
    * Creates a new entry builder.
    * @return the new builder
    */
   public abstract EntryBuilder newEntry();

   @Override
   public Object toObjectImpl() {
      return map;
   }
}
