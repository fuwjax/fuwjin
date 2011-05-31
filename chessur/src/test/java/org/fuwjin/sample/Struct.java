/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.sample;

/**
 * A sample class for object templating.
 */
public class Struct {
   /**
    * Creates a new instance directly for the matcher.
    * @param value the struct value
    * @return the new struct
    */
   public static Struct newInstance(final int value) {
      final Struct field = new Struct();
      field.value = value;
      return field;
   }

   /**
    * The int field.
    */
   public int value;

   @Override
   public boolean equals(final Object obj) {
      try {
         final Struct o = (Struct)obj;
         return getClass().equals(o.getClass()) && value == o.value;
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return value;
   }

   @Override
   public String toString() {
      return "Sample:" + value;
   }
}
