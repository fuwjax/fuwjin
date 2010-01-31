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
 * A simple self-referencing class
 * @author michaeldoberenz
 */
public class SelfReferencingObject {
   private final SelfReferencingObject obj;

   /**
    * creates a new self-referencing object
    */
   public SelfReferencingObject() {
      obj = this;
   }

   @Override
   public boolean equals(final Object object) {
      try {
         final SelfReferencingObject o = (SelfReferencingObject)object;
         return o.obj == obj;
      } catch(final RuntimeException e) {
         return false;
      }
   }

   /**
    * Returns the self reference.
    * @return the self reference
    */
   public Object getSelf() {
      return obj;
   }

   @Override
   public int hashCode() {
      return System.identityHashCode(obj);
   }
}
