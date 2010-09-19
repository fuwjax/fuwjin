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

import org.fuwjin.jon.JonLiteral;
import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.LiteralReference;
import org.fuwjin.jon.ref.ReferenceStorage;

/**
 * Builds a Class instance.
 */
public class ClassBuilder extends LiteralBuilder {
   private Class<?> cls;

   /**
    * Creates a new instance.
    * @param type expected Class.class
    */
   public ClassBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> expCls, final ReferenceStorage storage) {
      return new LiteralReference(storage.nextName(), null, JonLiteral.getName((Class<?>)obj));
   }

   @Override
   public void set(final String value) {
      cls = JonLiteral.forName(value);
   }

   @Override
   public Class<?> toObjectImpl() {
      return cls;
   }
}
