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

import java.util.Arrays;

/**
 * 4th order nesting situation with hiding
 * @author michaeldoberenz
 */
public class SimpleGreatGrandChild extends SimpleGrandChild {
   private final String[] s;

   /**
    * Creates a default instance
    */
   public SimpleGreatGrandChild() {
      super(57.354, 235, "nestedness"); //$NON-NLS-1$
      s = new String[]{"crazy", "train"}; //$NON-NLS-1$ //$NON-NLS-2$
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final SimpleGreatGrandChild o = (SimpleGreatGrandChild)obj;
         return Arrays.equals(s, o.s) && super.equals(obj);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(s) + super.hashCode();
   }
}
