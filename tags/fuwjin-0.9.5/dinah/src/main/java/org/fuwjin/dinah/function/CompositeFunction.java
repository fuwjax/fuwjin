/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.dinah.function;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.util.Adapter.AdaptException;

/**
 * A container for Function composition. While it is presumed that all functions
 * will share the same name, this is not required.
 */
public class CompositeFunction extends AbstractFunction {
   private final List<AbstractFunction> functions;

   /**
    * Creates a new instance. The name of this function will be the name of the
    * first supplied function.
    * @param functions the set of functions, must be at least 1
    */
   public CompositeFunction(final AbstractFunction... functions) {
      super(functions[0].name());
      this.functions = new ArrayList<AbstractFunction>(Arrays.asList(functions));
   }

   @Override
   public Object invoke(final Object... args) throws AdaptException, InvocationTargetException {
      AdaptException failure = null;
      for(final AbstractFunction function: functions) {
         try {
            return function.invoke(args);
         } catch(final AdaptException e) {
            failure = e;
         }
      }
      if(failure == null) {
         throw new AdaptException("Impossible Exception: there must always be at least one function");
      }
      throw failure;
   }

   @Override
   public boolean isPrivate() {
      for(final AbstractFunction function: functions) {
         if(!function.isPrivate()) {
            return false;
         }
      }
      return true;
   }

   @Override
   public AbstractFunction restrict(final FunctionSignature signature) {
      AbstractFunction accessible = null;
      AbstractFunction restricted = AbstractFunction.NULL;
      for(final AbstractFunction function: functions) {
         final AbstractFunction func = function.restrict(signature);
         restricted = restricted.join(func);
         accessible = moreAccessible(accessible, func);
      }
      if(accessible != null && accessible != this) {
         return accessible;
      }
      return restricted;
   }

   @Override
   protected AbstractFunction joinImpl(final AbstractFunction next) {
      functions.add(next);
      return this;
   }

   private AbstractFunction moreAccessible(final AbstractFunction current, final AbstractFunction func) {
      if(func.isPrivate()) {
         return current;
      }
      if(current == null) {
         return func;
      }
      if(func instanceof FixedArgsFunction && current instanceof FixedArgsFunction) {
         final FixedArgsFunction<?> c = (FixedArgsFunction<?>)current;
         final FixedArgsFunction<?> f = (FixedArgsFunction<?>)func;
         if(f.member() != null && c.member() != null
               && !c.member().getDeclaringClass().equals(f.member().getDeclaringClass())) {
            if(c.member().getDeclaringClass().isAssignableFrom(f.member().getDeclaringClass())) {
               return func;
            }
            if(f.member().getDeclaringClass().isAssignableFrom(c.member().getDeclaringClass())) {
               return current;
            }
         }
      }
      return this;
   }
}
