/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
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
