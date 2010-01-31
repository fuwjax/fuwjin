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
 * Simple subclass that hides a parent field
 * @author michaeldoberenz
 */
public class SimpleChild extends SimpleObject {
   private final double s;

   /**
    * creates a default instance
    */
   public SimpleChild() {
      super(181, "curious"); //$NON-NLS-1$
      s = 12.234;
   }

   /**
    * constructor for subclasses
    * @param d the value of this instances s
    * @param i the parent io
    * @param string the parent s
    */
   protected SimpleChild(final double d, final int i, final String string) {
      super(i, string);
      s = d;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final SimpleChild o = (SimpleChild)obj;
         return s == o.s && super.equals(obj);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() + (int)Double.doubleToLongBits(s);
   }
}
