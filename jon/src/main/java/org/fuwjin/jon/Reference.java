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
package org.fuwjin.jon;

public class Reference {
   private String name;
   private Object value;

   public Reference(final Object value) {
      this.value = value;
   }

   public Reference(final String name, final Object value) {
      this.name = name;
      this.value = value;
   }

   public String name() {
      return name;
   }

   public Object value() {
      return value;
   }
}