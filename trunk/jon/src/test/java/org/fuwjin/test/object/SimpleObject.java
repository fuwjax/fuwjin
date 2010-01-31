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
 * A sample serializable object for the JON tests.
 * @author michaeldoberenz
 */
public class SimpleObject {
   private final String s;
   private final IntegerObject io;

   /**
    * Creates a default simple object
    */
   public SimpleObject() {
      s = "howdy"; //$NON-NLS-1$
      io = new IntegerObject(17);
   }

   /**
    * Constructor for more complex objects
    * @param string the value of the string member
    * @param child the value of the integer object member
    */
   public SimpleObject(final String string, final IntegerObject child) {
      s = string;
      io = child;
   }

   /**
    * Constructor for child objects
    * @param i the internal value of the integer object memeber
    * @param string the value of the string member
    */
   protected SimpleObject(final int i, final String string) {
      s = string;
      io = new IntegerObject(i);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final SimpleObject o = (SimpleObject)obj;
         return s == null ? o.s == null : s.equals(o.s) && io == null ? o.io == null : io.equals(o.io);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      try {
         return io.hashCode() + s.hashCode();
      } catch(final RuntimeException e) {
         return super.hashCode();
      }
   }
}
