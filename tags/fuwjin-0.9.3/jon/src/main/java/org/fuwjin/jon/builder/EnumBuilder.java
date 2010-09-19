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

import static java.lang.Enum.valueOf;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.LiteralReference;
import org.fuwjin.jon.ref.ReferenceStorage;

/**
 * Builds an enum value.
 */
public class EnumBuilder extends LiteralBuilder {
   private Enum<?> value;

   /**
    * Creates a new instance.
    * @param type the enum type
    */
   public EnumBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return new LiteralReference(null, cast(storage, obj, cls), obj);
   }

   @Override
   public void set(final String value) {
      this.value = valueOf((Class)type(), value);
   }

   @Override
   public Enum<?> toObjectImpl() {
      return value;
   }
}
