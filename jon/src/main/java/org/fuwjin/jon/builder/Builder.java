/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon.builder;

import static org.fuwjin.jon.BuilderRegistry.getBuilder;
import static org.fuwjin.postage.Postage.isSuccess;

import org.fuwjin.jon.builder.util.UnsafeFactory;
import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.postage.ClassFunction;
import org.fuwjin.postage.Function;

public abstract class Builder {
   private final Class<?> type;
   private Class<?> verified;
   private Function invoker;
   private Builder builder;

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
         invoker = new ClassFunction(type, "new");
      }
      final Object ret = invoker.invokeSafe(args);
      if(!isSuccess(ret) && args.length == 0) {
         return UnsafeFactory.create(type);
      }
      return ret;
   }

   public abstract BaseReference newReference(Object obj, Class<?> cls, ReferenceStorage storage);

   public Object toObject() {
      return builder.toObjectImpl();
   }

   protected abstract Object toObjectImpl();

   protected Class<?> type() {
      return type;
   }

   public void verify(final Class<?> test) {
      if(!type.isAssignableFrom(test)) {
         throw new IllegalArgumentException();
      }
      verified = test;
   }
}
