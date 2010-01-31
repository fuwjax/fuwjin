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

import java.io.Serializable;

/**
 * A subclass of integer object with no serialized fields
 */
public class TransientChild extends IntegerObject implements Serializable {
   private static final long serialVersionUID = 1L;
   private transient final String alsoIgnored = "beta"; //$NON-NLS-1$

   /**
    * Creates a new instance
    */
   public TransientChild() {
      super(191);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final TransientChild o = (TransientChild)obj;
         return alsoIgnored.equals(o.alsoIgnored) && super.equals(obj);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() + alsoIgnored.hashCode();
   }
}
