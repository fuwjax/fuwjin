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
package org.fuwjin.jon.builder;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.postage.CompositeFunction;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.function.FieldMutatorFunction;

/**
 * Builds a new function.
 */
public class InvokerBuilder extends LiteralBuilder {
   private Function value;

   /**
    * Creates a new instance.
    * @param type the function's category
    */
   public InvokerBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public Builder forTypeImpl(final Class<?> newType) {
      return new InvokerBuilder(newType);
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return null;
   }

   @Override
   public void set(final String value) {
      this.value = new CompositeFunction(value, new FieldMutatorFunction(type(), value));
   }

   @Override
   public Function toObjectImpl() {
      return value;
   }
}
