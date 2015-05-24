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
package org.fuwjin.postage.type;

import java.lang.reflect.Type;

/**
 * An optional type for Function resolution.
 */
public class Optional extends TypeProxy {
   /**
    * A optional any object.
    */
   public static final Optional OBJECT = new Optional(Object.class);
   public static final Object UNSET = new Object();

   /**
    * Creates a new instance.
    * @param type the optional type
    */
   public Optional(final Type type) {
      super(type);
   }

   @Override
   public boolean isAssignableFrom(final Class<?> test) {
      if(void.class.equals(test)) {
         return true;
      }
      return super.isAssignableFrom(test);
   }

   @Override
   public boolean isAssignableTo(final Type test) {
      if(void.class.equals(test)) {
         return true;
      }
      return super.isAssignableTo(test);
   }

   @Override
   public String toString() {
      return type() + "?";
   }
}
