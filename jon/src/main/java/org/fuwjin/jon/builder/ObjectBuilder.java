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

import static org.fuwjin.jon.BuilderRegistry.getBuilder;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.pogo.reflect.invoke.Invoker;

public class ObjectBuilder extends EntriesBuilder {
   public ObjectBuilder(final Class<?> type, final Object obj) {
      super(type, obj);
   }

   @Override
   public EntryBuilder newEntry() {
      return new EntryBuilder() {
         @Override
         public Builder newKey() {
            return new InvokerBuilder(type());
         }

         @Override
         public Builder newValue() {
            return getBuilder(invoker().paramTypes(1)[0]);
         }

         @Override
         public void storeImpl() {
            invoker().invoke(target, value);
         }

         private Invoker invoker() {
            return (Invoker)key;
         }
      };
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return null;
   }
}
