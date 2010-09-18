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
package org.fuwjin.jon.builder;

/**
 * Builds a literal.
 */
public abstract class LiteralBuilder extends Builder {
   /**
    * Creates a new instance.
    * @param type the literal type
    */
   public LiteralBuilder(final Class<?> type) {
      super(type);
   }

   /**
    * Sets the serialized form of the literal.
    * @param value the serialized literal
    */
   public abstract void set(final String value);
}
