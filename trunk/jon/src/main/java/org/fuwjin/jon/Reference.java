/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

/**
 * Manages the references.
 */
public class Reference {
   private String name;
   private Object value;

   /**
    * Creates a new instance.
    */
   public Reference() {
      // do nothing
   }

   /**
    * Creates a new instance.
    * @param value the reference value
    */
   public Reference(final Object value) {
      this.value = value;
   }

   /**
    * Returns the reference name.
    * @return the reference name
    */
   public String name() {
      return name;
   }

   /**
    * Sets the reference name.
    * @param newName the new name
    */
   protected void name(final String newName) {
      assert this.name == null;
      this.name = newName;
   }

   /**
    * Returns the reference value.
    * @return the reference value
    */
   public Object value() {
      return value;
   }

   /**
    * Sets the reference value.
    * @param newValue the new value
    */
   protected void value(final Object newValue) {
      assert this.value == null;
      this.value = newValue;
   }
}