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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A subclass of integer object with a list of strings
 */
public class IntegerChild extends IntegerObject {
   private final List<String> list;

   /**
    * Creates a new instance
    * @param strings an array of strings used to populate the list
    * @param id the parent's i field
    */
   public IntegerChild(final String[] strings, final int id) {
      super(id);
      list = new ArrayList<String>(Arrays.asList(strings));
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final IntegerChild o = (IntegerChild)obj;
         return list.equals(o.list) && super.equals(obj);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() + list.hashCode();
   }
}
