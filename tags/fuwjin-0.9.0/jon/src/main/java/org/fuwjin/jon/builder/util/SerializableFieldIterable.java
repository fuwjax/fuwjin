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

import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SerializableFieldIterable implements Iterable<Field> {
   static boolean isSerializable(final Field field) {
      return !isStatic(field.getModifiers()) && !isTransient(field.getModifiers());
   }

   private final Field[] fields;

   public SerializableFieldIterable(final Class<?> type) {
      fields = type.getDeclaredFields();
   }

   @Override
   public Iterator<Field> iterator() {
      return new Iterator<Field>() {
         private int index = nextField(0);

         @Override
         public boolean hasNext() {
            return index < fields.length;
         }

         @Override
         public Field next() {
            if(!hasNext()) {
               throw new NoSuchElementException();
            }
            final int ret = index;
            index = nextField(index + 1);
            return fields[ret];
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }

         private int nextField(final int i) {
            int newIndex = i;
            while(newIndex < fields.length && !isSerializable(fields[newIndex])) {
               newIndex++;
            }
            return newIndex;
         }
      };
   }
}