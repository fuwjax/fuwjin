/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.jon.ref;

/**
 * Manages the reference for a literal.
 */
public class LiteralReference extends BaseReference {
   private final Object value;

   /**
    * Creates a new instance.
    * @param name the name of the reference
    * @param type the type of the literal
    * @param value the literal value
    */
   public LiteralReference(final String name, final Object type, final Object value) {
      super(name, type);
      this.value = value;
   }

   /**
    * Returns the value.
    * @return the value
    */
   public Object get() {
      return value;
   }
}
