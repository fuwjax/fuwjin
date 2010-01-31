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
package org.fuwjin.jon.builder;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.pogo.reflect.invoke.Invoker;

public class PrimitiveBuilder extends LiteralBuilder {
   private static final Map<Class<?>, Class<?>> WRAPPERS;
   static {
      WRAPPERS = new HashMap<Class<?>, Class<?>>();
      WRAPPERS.put(int.class, Integer.class);
      WRAPPERS.put(short.class, Short.class);
      WRAPPERS.put(boolean.class, Boolean.class);
      WRAPPERS.put(byte.class, Byte.class);
      WRAPPERS.put(long.class, Long.class);
      WRAPPERS.put(double.class, Double.class);
      WRAPPERS.put(float.class, Float.class);
      WRAPPERS.put(char.class, Character.class);
   }

   public static boolean isWrapper(final Class<?> test) {
      return WRAPPERS.containsValue(test);
   }

   private Object value;
   private final Invoker invoker;

   public PrimitiveBuilder(final Class<?> type) {
      super(type);
      final Class<?> target = WRAPPERS.get(type);
      invoker = new Invoker(target == null ? type : target, "valueOf");
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return null;
   }

   @Override
   public void set(final String value) {
      this.value = invoker.invoke(null, value);
   }

   @Override
   public Object toObjectImpl() {
      return value;
   }
}
