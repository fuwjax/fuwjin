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
package org.fuwjin.jon.builder.util;

import static org.fuwjin.jon.builder.util.SerializableFieldIterable.isSerializable;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over the super classes.
 */
public class SuperclassIterable implements Iterable<Class<?>> {
   private static Class<?> getSuperclass(final Class<?> type) {
      Class<?> next = type;
      while(next != null && !hasSerializableFields(next)) {
         next = next.getSuperclass();
      }
      return next;
   }

   private static boolean hasSerializableFields(final Class<?> next) {
      for(final Field field: next.getDeclaredFields()) {
         if(isSerializable(field)) {
            return true;
         }
      }
      return false;
   }

   private final Class<?> type;

   /**
    * Creates a new instance.
    * @param type the class type
    */
   public SuperclassIterable(final Class<?> type) {
      this.type = getSuperclass(type);
   }

   @Override
   public Iterator<Class<?>> iterator() {
      return new Iterator<Class<?>>() {
         private Class<?> next = type;

         @Override
         public boolean hasNext() {
            return next != null;
         }

         @Override
         public Class<?> next() {
            if(!hasNext()) {
               throw new NoSuchElementException();
            }
            final Class<?> ret = next;
            next = getSuperclass(next.getSuperclass());
            return ret;
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }
}
