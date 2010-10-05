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

import static org.fuwjin.jon.BuilderRegistry.getBuilder;
import static org.fuwjin.postage.Postage.isSuccess;

import org.fuwjin.jon.builder.util.UnsafeFactory;
import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.category.ClassCategory;

/**
 * The base class for object builders.
 */
public abstract class Builder {
   private final Class<?> type;
   private Class<?> verified;
   private Function invoker;
   private Builder builder;

   /**
    * Creates a new instance.
    * @param type the type to build
    */
   public Builder(final Class<?> type) {
      this.type = type;
      builder = this;
   }

   protected Object cast(final ReferenceStorage storage, final Object obj, final Class<?> expected) {
      if(!obj.getClass().equals(expected)) {
         return storage.get(obj.getClass(), Class.class);
      }
      return null;
   }

   /**
    * Creates a new builder for the refined type.
    * @return the new builder
    */
   public Builder forType() {
      if(verified != null && !verified.equals(type)) {
         builder = forTypeImpl(verified);
      }
      return builder;
   }

   protected Builder forTypeImpl(final Class<?> newType) {
      return getBuilder(newType);
   }

   protected Object newInstance(final Object... args) {
      if(invoker == null) {
         invoker = new ClassCategory(type).getFunction("new");
      }
      final Object ret = invoker.invokeSafe(args);
      if(!isSuccess(ret) && args.length == 0) {
         return UnsafeFactory.create(type);
      }
      return ret;
   }

   /**
    * Creates a new reference.
    * @param obj the object to reference
    * @param cls the object type
    * @param storage the storage
    * @return the new reference
    */
   public abstract BaseReference newReference(Object obj, Class<?> cls, ReferenceStorage storage);

   /**
    * Returns the built object.
    * @return the built object
    */
   public Object toObject() {
      return builder.toObjectImpl();
   }

   protected abstract Object toObjectImpl();

   protected Class<?> type() {
      return type;
   }

   /**
    * Verifies a refined type.
    * @param test the new type
    */
   public void verify(final Class<?> test) {
      if(!type.isAssignableFrom(test)) {
         throw new IllegalArgumentException();
      }
      verified = test;
   }
}
