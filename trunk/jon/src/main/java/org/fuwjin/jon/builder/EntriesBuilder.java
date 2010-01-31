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

public abstract class EntriesBuilder extends Builder {
   public static abstract class EntryBuilder {
      protected Object key;
      protected Object value;
      protected Object target;

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

      public abstract Builder newKey();

      public abstract Builder newValue();

      public void store(final Object map) {
         target = map;
         finish();
      }

      public abstract void storeImpl();

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

      private void finish() {
         if(target == null || key instanceof ContainerProxy || value instanceof ContainerProxy) {
            return;
         }
         storeImpl();
      }
   }

   protected Object map;

   public EntriesBuilder(final Class<?> type) {
      super(type);
      map = newInstance();
   }

   public EntriesBuilder(final Class<?> type, final Object map) {
      super(type);
      this.map = map;
   }

   public void add(final EntryBuilder entry) {
      entry.store(map);
   }

   public abstract EntryBuilder newEntry();

   @Override
   public Object toObjectImpl() {
      return map;
   }
}
