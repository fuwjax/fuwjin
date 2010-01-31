/*
 * This file is part of The Fuwjin Suite.
 *
 * The Fuwjin Suite is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Fuwjin Suite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2008 Michael Doberenz
 */
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