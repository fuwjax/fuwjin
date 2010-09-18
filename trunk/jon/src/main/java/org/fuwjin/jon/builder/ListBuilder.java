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

import java.util.List;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ListReference;
import org.fuwjin.jon.ref.ReferenceStorage;

/**
 * Builds a list.
 */
public class ListBuilder extends ElementsBuilder {
   /**
    * Creates a new instance.
    * @param type the list type
    */
   public ListBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public Builder newElement() {
      return null;
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return new ListReference(storage.nextName(), storage, cast(storage, obj, cls), (List<?>)obj, null);
   }

   @Override
   protected void postAdd(final int index, final Object value) {
      list.set(index, value);
   }

   @Override
   protected Object toObjectImpl() {
      return list;
   }
}
