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
 * Simple class that has a single primitive-wrapper field
 * @author michaeldeck
 */
public class IntegerField {
   private final Integer i;

   /**
    * create a default IntegerField object
    */
   public IntegerField() {
      i = new Integer(5);
   }

   /**
    * create an IntegerField object whose internal field is set to i
    * @param i the value of the objects internal field
    */
   public IntegerField(final Integer i) {
      this.i = i;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final IntegerField o = (IntegerField)obj;
         return i == o.i;
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return i.hashCode();
   }
}
