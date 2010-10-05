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

public class TypeProxy implements ExtendedType {
   private final Type type;

   public TypeProxy(final Type type) {
      this.type = type;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final TypeProxy o = (TypeProxy)obj;
         return getClass().equals(o.getClass()) && type.equals(o.type);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Type getComponentType() {
      return TypeUtils.getComponentType(type);
   }

   @Override
   public Type[] getUpperBounds() {
      return TypeUtils.getUpperBounds(type);
   }

   @Override
   public int hashCode() {
      return type.hashCode();
   }

   @Override
   public boolean isAssignableFrom(final Class<?> test) {
      return TypeUtils.isAssignableFrom(type, test);
   }

   @Override
   public boolean isAssignableTo(final Type test) {
      return TypeUtils.isAssignableTo(type, test);
   }

   @Override
   public boolean isClass() {
      return TypeUtils.isClass(type);
   }

   @Override
   public boolean isInstance(final Object object) {
      return TypeUtils.isInstance(type, object);
   }

   protected Type type() {
      return type;
   }
}
