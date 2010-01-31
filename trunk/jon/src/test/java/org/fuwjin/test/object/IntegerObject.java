/*
 * This file is part of JON.
 *
 * JON is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * JON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2007 Michael Doberenz
 */
package org.fuwjin.test.object;

/**
 * another test class for JON
 * @author michaeldoberenz
 */
public class IntegerObject {
   private final int i;

   /**
    * Creates a default integer object
    */
   public IntegerObject() {
      i = 15;
   }

   /**
    * Creates a wrapper for the given int
    * @param i the int to wrap
    */
   public IntegerObject(final int i) {
      this.i = i;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final IntegerObject o = (IntegerObject)obj;
         return i == o.i;
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return i;
   }
}
