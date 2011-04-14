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
import java.lang.reflect.Member;

/**
 * Function for reflective static field access.
 */
public class StaticFieldAccessFunction extends FixedArgsFunction {
   private final Field field;

   /**
    * Creates a new instance.
    * @param category the function category
    * @param field the field to access
    */
   public StaticFieldAccessFunction(final String category, final Field field) {
      super(category + '.' + field.getName());
      this.field = field;
   }

   @Override
   protected Object invokeSafe(final Object... args) throws IllegalArgumentException, IllegalAccessException {
      return field.get(null);
   }

   @Override
   protected Member member() {
      return field;
   }
}
