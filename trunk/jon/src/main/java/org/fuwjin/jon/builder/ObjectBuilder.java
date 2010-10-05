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

import static org.fuwjin.jon.BuilderRegistry.getBuilder;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.postage.Function;

/**
 * Builds an object.
 */
public class ObjectBuilder extends EntriesBuilder {
   /**
    * Creates a new instance.
    * @param type the object type
    * @param obj the object
    */
   public ObjectBuilder(final Class<?> type, final Object obj) {
      super(type, obj);
   }

   @Override
   public EntryBuilder newEntry() {
      return new EntryBuilder() {
         private Function invoker() {
            return (Function)key;
         }

         @Override
         public Builder newKey() {
            return new InvokerBuilder(type());
         }

         @Override
         public Builder newValue() {
            return getBuilder((Class<?>)invoker().parameterType(1));
         }

         @Override
         public void storeImpl() {
            invoker().invokeSafe(target, value);
         }
      };
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return null;
   }
}
