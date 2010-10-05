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

import java.util.Iterator;

import org.fuwjin.jon.builder.util.SuperclassIterable;
import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.InstanceReference;
import org.fuwjin.jon.ref.ReferenceStorage;

/**
 * Builds an object instance.
 */
public class InstanceBuilder extends Builder implements Iterable<ObjectBuilder> {
   private final Object obj;
   private final Iterable<Class<?>> supers;

   /**
    * Creates a new instance.
    * @param type the object type
    */
   public InstanceBuilder(final Class<?> type) {
      super(type);
      obj = newInstance();
      supers = new SuperclassIterable(type());
   }

   @Override
   public Iterator<ObjectBuilder> iterator() {
      final Iterator<Class<?>> iter = supers.iterator();
      return new Iterator<ObjectBuilder>() {
         @Override
         public boolean hasNext() {
            return iter.hasNext();
         }

         @Override
         public ObjectBuilder next() {
            return superBuilder(iter.next());
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   @Override
   public BaseReference newReference(final Object refObj, final Class<?> cls, final ReferenceStorage storage) {
      return new InstanceReference(storage.nextName(), storage, cast(storage, refObj, cls), refObj);
   }

   ObjectBuilder superBuilder(final Class<?> superClass) {
      return new ObjectBuilder(superClass, obj);
   }

   @Override
   public Object toObjectImpl() {
      return obj;
   }
}
