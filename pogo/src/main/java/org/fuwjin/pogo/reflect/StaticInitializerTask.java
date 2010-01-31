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
package org.fuwjin.pogo.reflect;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.reflect.invoke.Invoker;

/**
 * Creates a new context from a dispatch method.
 */
public class StaticInitializerTask implements InitializerTask {
   private static final String ERROR_CODE = "IC-"; //$NON-NLS-1$
   private static final String FAILED_STATIC_INVOKE = "failed static invoke"; //$NON-NLS-1$
   private static final String FAILED_BOOLEAN_CALL = "failed boolean call"; //$NON-NLS-1$
   private String name;
   private transient Invoker invoker;
   private ReflectionType type;

   /**
    * Creates a new instance.
    * @param name the name of the message to dispatch
    */
   public StaticInitializerTask(final String name) {
      this.name = name;
   }

   /**
    * Creates a new instance.
    */
   StaticInitializerTask() {
      // for reflection
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final StaticInitializerTask o = (StaticInitializerTask)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), name);
   }

   @Override
   public PogoContext initialize(final PogoContext input) {
      final Object obj = input.get();
      if(obj != null && !type.isInstance(obj)) {
         final Object result = invoker.invoke(obj, obj);
         return input.newChild(result, isSuccess(result), ERROR_CODE + type);
      }
      final Object result = invoker.invoke(obj);
      if(result instanceof Boolean) {
         return input.newChild(obj, (Boolean)result, FAILED_BOOLEAN_CALL);
      }
      return input.newChild(result, isSuccess(result), FAILED_STATIC_INVOKE);
   }

   @Override
   public void setType(final ReflectionType type) {
      this.type = type;
      invoker = type.getInvoker(name);
   }

   @Override
   public String toString() {
      return name;
   }
}
