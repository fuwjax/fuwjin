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

import static java.util.Collections.emptyList;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Method dispatch for java.
 */
public class Invoker implements InvokeHandler {
   private static final String FAILED_INVOCATION = "Failed invocation"; //$NON-NLS-1$
   private static final String NEW = "new"; //$NON-NLS-1$
   /**
    * A no-op dispatcher.
    */
   public static final Invoker NULL = new Invoker();

   /**
    * Returns true if obj is the result of a successful dispatch.
    * @param obj the test object
    * @return false if obj is FAILURE, true otherwise
    */
   public static boolean isSuccess(final Object obj) {
      return obj != FAILURE;
   }

   private final List<InvokeHandler> handlers;

   /**
    * Creates a new instance.
    * @param type the type responsible for the dispatch
    * @param name the dispatched message
    */
   public Invoker(final Class<?> type, final String name) {
      handlers = new LinkedList<InvokeHandler>();
      if(NEW.equals(name)) {
         for(final Constructor<?> constructor: type.getDeclaredConstructors()) {
            handlers.add(new ConstructorInvokeHandler(constructor));
         }
      } else {
         addMethods(name, type);
         Class<?> cls = type;
         while(cls != null) {
            try {
               final Field field = cls.getDeclaredField(name);
               handlers.add(new FieldInvokeHandler(field));
            } catch(final Exception e) {
               // continue
            }
            cls = cls.getSuperclass();
         }
      }
   }

   private Invoker() {
      handlers = emptyList();
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final Invoker o = (Invoker)obj;
         return eq(getClass(), o.getClass()) && eq(handlers, o.handlers);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), handlers);
   }

   public Object invoke(final Object target, final Object... args) {
      for(final InvokeHandler handler: handlers) {
         try {
            final Object ret = handler.invoke(target, args);
            if(isSuccess(ret)) {
               return ret;
            }
         } catch(final Exception e) {
            throw new RuntimeException(FAILED_INVOCATION, e);
         }
      }
      return FAILURE;
   }

   public Class<?>[] paramTypes(final int paramCount) {
      for(final InvokeHandler handler: handlers) {
         final Class<?>[] types = handler.paramTypes(paramCount);
         if(types != null) {
            return types;
         }
      }
      return null;
   }

   @Override
   public String toString() {
      return handlers.toString();
   }

   private void addMethods(final String name, final Class<?> cls) {
      if(cls != null) {
         for(final Method method: cls.getDeclaredMethods()) {
            if(method.getName().equals(name)) {
               handlers.add(new MethodInvokeHandler(method));
            }
         }
         for(final Class<?> iface: cls.getInterfaces()) {
            addMethods(name, iface);
         }
         addMethods(name, cls.getSuperclass());
      }
   }
}
