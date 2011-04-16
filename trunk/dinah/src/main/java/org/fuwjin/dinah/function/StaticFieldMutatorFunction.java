/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.dinah.function;

import java.lang.reflect.Field;
import org.fuwjin.util.Adapter;

/**
 * Function for reflective static field mutation.
 */
public class StaticFieldMutatorFunction extends FixedArgsFunction<Field> {
   /**
    * Creates a new instance.
    * @param category the function category
    * @param field the field to mutate
    */
   public StaticFieldMutatorFunction(final String category, final Field field) {
      super(field, category + '.' + field.getName(), field.getType());
   }

   @Override
   protected Object invokeSafe(final Object... args) throws IllegalArgumentException, IllegalAccessException {
      member().set(null, args[0]);
      return Adapter.unset();
   }
}
