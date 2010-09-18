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
package org.fuwjin.jon.ref;

/**
 * Manages a string reference.
 */
public class StringReference extends BaseReference {
   private final String value;

   /**
    * Creates a new instance.
    * @param name the reference name
    * @param type the string type
    * @param value the value
    */
   public StringReference(final String name, final Object type, final Object value) {
      super(name, type);
      this.value = value.toString();
   }

   /**
    * Returns the value.
    * @return the value
    */
   public Object get() {
      return value;
   }
}
