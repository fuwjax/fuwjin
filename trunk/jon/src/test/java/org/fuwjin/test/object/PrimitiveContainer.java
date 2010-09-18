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
 * A wrapper around a primitive.
 */
public class PrimitiveContainer {
   private final int index;

   /**
    * Creates a new instance.
    * @param index the primitive
    */
   public PrimitiveContainer(final int index) {
      this.index = index;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final PrimitiveContainer o = (PrimitiveContainer)obj;
         return index == o.index;
      } catch(final ClassCastException e) {
         return false;
      }
   }
}
