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
package org.fuwjin.pogo.reflect.invoke;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.lang.reflect.Field;

/**
 * An InvokeHandler that targets a field.
 */
public class FieldInvokeHandler implements InvokeHandler {
   private final Field field;

   /**
    * Creates a new instance.
    * @param field the target field
    */
   public FieldInvokeHandler(final Field field) {
      field.setAccessible(true);
      this.field = field;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final FieldInvokeHandler o = (FieldInvokeHandler)obj;
         return eq(getClass(), o.getClass()) && eq(field, o.field);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), field);
   }

   @Override
   public Object invoke(final Object target, final Object... args) throws Exception {
      try {
         if(args.length == 0) {
            return field.get(target);
         }
         if(args.length == 1) {
            field.set(target, args[0]);
            return null;
         }
      } catch(final IllegalArgumentException e) {
         // continue
      }
      return FAILURE;
   }

   @Override
   public Class<?>[] paramTypes(final int paramCount) {
      if(paramCount == 0) {
         return new Class<?>[0];
      }
      if(paramCount == 1) {
         return new Class<?>[]{field.getType()};
      }
      return null;
   }

   @Override
   public String toString() {
      return field.toString();
   }
}
