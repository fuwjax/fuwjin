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
 * Test class with a non-static inner class member
 * @author michaeldoberenz
 */
public class InnerChild {
   /**
    * Non static class with both a synthetic and real field
    * @author michaeldoberenz
    */
   public class Child {
      private final int i;

      /**
       * Creates a new instance
       */
      public Child() {
         i = 1;
      }

      @Override
      public boolean equals(final Object obj) {
         try {
            final Child o = (Child)obj;
            return o.i == i;
         } catch(final RuntimeException e) {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return i;
      }
   }

   private final Child inner;

   /**
    * Creates a new instance
    */
   public InnerChild() {
      inner = new Child();
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InnerChild o = (InnerChild)obj;
         return inner.equals(o.inner);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   /**
    * Returns the inner child.
    * @return the inner child
    */
   public Child getChild() {
      return inner;
   }

   @Override
   public int hashCode() {
      return inner.hashCode();
   }
}
