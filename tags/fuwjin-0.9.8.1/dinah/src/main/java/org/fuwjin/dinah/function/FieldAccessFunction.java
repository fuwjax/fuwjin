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
import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.signature.FixedArgsSignature;

/**
 * Function for reflective field access.
 */
public class FieldAccessFunction extends MemberFunction<Field> {
   /**
    * Creates a new instance.
    * @param adapter the type converter
    * @param category the function category
    * @param field the field instance
    * @param type the object type required to access the field
    */
   public FieldAccessFunction(final Adapter adapter, final Type category, final Field field) {
      super(field, new FixedArgsSignature(adapter, category, field.getName(), field.getType(), category));
   }

   @Override
   protected Object invokeSafe(final Object... args) throws IllegalAccessException {
      return member().get(args[0]);
   }
}
