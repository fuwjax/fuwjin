/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.reflect;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.category.ClassCategory;

/**
 * The standard class based reflection type.
 */
public class ClassType implements ReflectionType {
   private Class<?> type;
   private ClassCategory category;

   /**
    * Creates a new instance.
    */
   ClassType() {
      // for reflection
   }

   /**
    * Creates a new instance.
    * @param type the type to reflect on
    */
   public ClassType(final Class<?> type) {
      this.type = type;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ClassType o = (ClassType)obj;
         return eq(getClass(), o.getClass()) && eq(type, o.type);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public Function getInvoker(final String name) {
      category = new ClassCategory(type);
      return category.getFunction(name);
   }

   @Override
   public int hashCode() {
      return hash(getClass(), type);
   }

   @Override
   public boolean isInstance(final Object input) {
      return type.isInstance(input);
   }

   @Override
   public String toString() {
      if(type == null) {
         return "null";
      }
      return type.getName();
   }
}
