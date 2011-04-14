/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.dinah.function;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import org.fuwjin.util.Adapter;

/**
 * Function for reflective field mutation.
 */
public class FieldMutatorFunction extends FixedArgsFunction {
   private final Field field;

   /**
    * Creates a new instance.
    * @param category the function category
    * @param field the field to mutate
    * @param type the type of the host object
    */
   public FieldMutatorFunction(final String category, final Field field, final Type type) {
      super(category + '.' + field.getName(), type, field.getType());
      this.field = field;
   }

   @Override
   protected Object invokeSafe(final Object... args) throws IllegalArgumentException, IllegalAccessException {
      field.set(args[0], args[1]);
      return Adapter.unset();
   }

   @Override
   protected Member member() {
      return field;
   }
}
