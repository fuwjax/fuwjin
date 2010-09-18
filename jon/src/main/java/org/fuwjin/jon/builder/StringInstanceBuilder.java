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

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ReferenceStorage;

/**
 * Builds a string.
 */
public class StringInstanceBuilder extends LiteralBuilder {
   private String value;

   /**
    * Creates a new instance.
    * @param type the string type
    */
   public StringInstanceBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return null;
   }

   @Override
   public void set(final String value) {
      this.value = value;
   }

   @Override
   public String toObjectImpl() {
      return value;
   }
}
